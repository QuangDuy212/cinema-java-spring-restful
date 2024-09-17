package com.vn.cinema_internal_java_spring_rest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Bill;
import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.bill.ResBillDTO;
import com.vn.cinema_internal_java_spring_rest.repository.BillRepository;
import com.vn.cinema_internal_java_spring_rest.repository.SeatRepository;
import com.vn.cinema_internal_java_spring_rest.repository.UserRepository;
import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;

    public BillService(BillRepository billRepository, UserRepository userRepository, SeatRepository seatRepository) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
    }

    public Bill handleCreateABill(Bill reqBill) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent())
            reqBill.setUser(user.get());
        // if (reqBill.getSeats() != null) {
        // List<Long> listIds = reqBill.getSeats()
        // .stream().map(i -> i.getId())
        // .collect(Collectors.toList());
        // List<Seat> seats = this.seatRepository.findByIdIn(listIds);
        // reqBill.setSeats(seats);
        // }
        return this.billRepository.save(reqBill);

    }

    public ResBillDTO converBillToResBillDTO(Bill bill) {
        ResBillDTO res = new ResBillDTO();
        ResBillDTO.BillUser user = new ResBillDTO.BillUser();
        res.setId(bill.getId());
        res.setQuantity(bill.getQuantity());
        res.setTotal(bill.getTotal());
        res.setStatus(bill.getStatus());
        user.setEmail(bill.getUser().getEmail());
        res.setUser(user);
        res.setCreatedAt(bill.getCreatedAt());
        res.setUpdatedAt(bill.getUpdatedAt());
        res.setCreatedBy(bill.getCreatedBy());
        res.setUpdatedBy(bill.getUpdatedBy());
        if (bill.getSeats() != null) {
            List<ResBillDTO.BillSeat> seats = new ArrayList<ResBillDTO.BillSeat>();
            for (Seat s : bill.getSeats()) {
                ResBillDTO.BillSeat seat = new ResBillDTO.BillSeat();
                seat.setId(s.getId());
                seat.setName(s.getName());
                seats.add(seat);
                res.setNameFilm(s.getShow().getFilm().getName());
                res.setShow(s.getShow().getTime());
                res.setZoomNumber(s.getShow().getZoomNumber());
                res.setDate(s.getShow().getDay().getDate());
            }
            res.setSeats(seats);
        }
        return res;
    }

    public Bill fetchBillById(long id) {
        Optional<Bill> billOptional = this.billRepository.findById(id);
        if (billOptional.isPresent())
            return billOptional.get();
        return null;
    }

    public ResultPaginationDTO fetchAllBills(Specification<Bill> spe, Pageable page) {
        Page<Bill> listBills = this.billRepository.findAll(spe, page);
        List<ResBillDTO> listResBills = listBills.getContent()
                .stream().map(i -> this.converBillToResBillDTO(i))
                .collect(Collectors.toList());
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listBills.getTotalPages());
        meta.setTotal(listBills.getTotalElements());

        res.setMeta(meta);
        res.setResult(listResBills);
        return res;
    }

    public List<ResBillDTO> fetchListBillsByUser() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        Optional<User> user = this.userRepository.findByEmail(email);
        List<Bill> listBills = this.billRepository.findByUser(user.get());
        List<ResBillDTO> res = listBills.stream().map(i -> this.converBillToResBillDTO(i))
                .collect(Collectors.toList());
        return res;
    }

    public Bill handleUpdateABill(Bill reqBill) {
        Bill currentBill = this.fetchBillById(reqBill.getId());
        if (reqBill.getStatus() != null)
            currentBill.setStatus(reqBill.getStatus());
        return this.billRepository.save(currentBill);
    }

    public void handleDeleteBill(long id) {
        this.billRepository.deleteById(id);
    }
}
