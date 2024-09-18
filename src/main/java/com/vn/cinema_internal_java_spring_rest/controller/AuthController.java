package com.vn.cinema_internal_java_spring_rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.auth.ReqLoginDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.auth.ResLoginDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResCreateUserDTO;
import com.vn.cinema_internal_java_spring_rest.service.UserService;
import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

        @Value("${quangduy.jwt.refresh-token-validity-in-seconds}")
        private long refreshTokenExpiration;
        private final SecurityUtil securityUtil;
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;

        public AuthController(SecurityUtil securityUtil, AuthenticationManagerBuilder authenticationManagerBuilder,
                        UserService userService, PasswordEncoder passwordEncoder) {
                this.securityUtil = securityUtil;
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.userService = userService;
                this.passwordEncoder = passwordEncoder;
        }

        @PostMapping("/auth/login")
        @ApiMessage("Login success")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) throws CommonException {
                // Nạp input gồm username/password vào Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(), loginDTO.getPassword());

                // xác thực người dùng => cần viết hàm loadUserByUsername
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                // save info auth into security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // return api
                ResLoginDTO res = new ResLoginDTO();
                User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
                if (currentUserDB == null || currentUserDB.isActive() == false)
                        throw new CommonException("Tài khoản không tồn tại!");
                if (currentUserDB != null) {
                        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                        currentUserDB.getId(),
                                        currentUserDB.getEmail(),
                                        currentUserDB.getFullName(),
                                        currentUserDB.getPhone(),
                                        currentUserDB.getAddress(),
                                        currentUserDB.getRole());
                        res.setUser(userLogin);
                }
                // create a token
                String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
                res.setAccessToken(access_token);

                // create refesh token
                String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

                this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

                // set cookies
                ResponseCookie resCookies = ResponseCookie
                                .from("refresh_token", refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                                .body(res);
        }

        @GetMapping("/auth/account")
        public ResponseEntity<ResLoginDTO.UserGetAccount> getMethodName() {
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                User currentUserDB = this.userService.handleGetUserByUsername(email);
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
                ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
                if (currentUserDB != null) {
                        userLogin.setId(currentUserDB.getId());
                        userLogin.setEmail(currentUserDB.getEmail());
                        userLogin.setFullName(currentUserDB.getFullName());
                        userLogin.setRole(currentUserDB.getRole());
                        userLogin.setAddress(currentUserDB.getAddress());
                        userLogin.setPhone(currentUserDB.getPhone());
                        userGetAccount.setUser(userLogin);
                }
                return ResponseEntity.ok().body(userGetAccount);
        }

        @GetMapping("/auth/refresh")
        @ApiMessage("Get User by fresh token")
        public ResponseEntity<ResLoginDTO> getRefreshToken(
                        @CookieValue(name = "refresh_token", defaultValue = "duy") String refresh_token)
                        throws CommonException {
                // check valid token
                if (refresh_token.equals("duy")) {
                        throw new CommonException("Bạn không có refresh_token ở cookies");
                }
                Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
                String email = decodedToken.getSubject();

                // check user by token + email ( 2nd layer check)
                User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
                if (currentUser == null) {
                        throw new CommonException("Refresh token không hợp lệ");
                }

                // issue new token/set refresh token as cookies
                ResLoginDTO res = new ResLoginDTO();
                User currentUserDB = this.userService.handleGetUserByUsername(email);
                if (currentUserDB != null) {
                        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                        currentUserDB.getId(),
                                        currentUserDB.getEmail(),
                                        currentUserDB.getFullName(),
                                        currentUserDB.getPhone(),
                                        currentUserDB.getAddress(),
                                        currentUserDB.getRole());
                        res.setUser(userLogin);
                }
                // create a token
                String access_token = this.securityUtil.createAccessToken(email, res);
                res.setAccessToken(access_token);

                // create refesh token
                String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

                this.userService.updateUserToken(new_refresh_token, email);

                // set cookies
                ResponseCookie resCookies = ResponseCookie
                                .from("refresh_token", new_refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                                .body(res);
        }

        @PostMapping("/auth/logout")
        @ApiMessage("Logout success")
        public ResponseEntity<Void> logout() throws CommonException {
                String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                if (email.equals("")) {
                        throw new CommonException("Access Token không hợp lệ");
                }
                User currentUserDB = this.userService.handleGetUserByUsername(email);
                if (currentUserDB != null) {
                        // set refresh token == null
                        this.userService.handleLogout(currentUserDB);
                }
                // remove refresh_token in cookies
                ResponseCookie deleteSpringCookie = ResponseCookie
                                .from("refresh_token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                                .body(null);
        }

        @PostMapping("/auth/register")
        @ApiMessage("Register a new user")
        public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody User user) throws CommonException {
                boolean isExists = this.userService.isExistByEmail(user.getEmail());
                if (isExists) {
                        throw new CommonException("Email đã tồn tại, vui lòng nhập lại!");
                }
                String hashPass = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashPass);
                User ericUser = this.userService.handleCreateUser(user);
                // convert to ResCreateUserDTO to display
                ResCreateUserDTO res = this.userService.convertUserToResUserCreateUserDTO(ericUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

}
