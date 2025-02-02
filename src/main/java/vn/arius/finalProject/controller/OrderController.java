package vn.arius.finalProject.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Order;
import vn.arius.finalProject.service.OrderService;
import vn.arius.finalProject.util.ExportOrder;
import vn.arius.finalProject.util.annotation.ApiMessage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OrderController {
    OrderService orderService;

    @PostMapping("/orders")
    @ApiMessage("Create a orders")
    public ResponseEntity<Order> createOrder(@RequestPart("receiverName") String receiverName,
                                             @RequestPart("receiverPhone") String receiverPhone,
                                             @RequestPart("receiverAddress") String receiverAddress) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.handleCreate(receiverName, receiverAddress, receiverPhone));

    }

    @GetMapping("/orders")
    @ApiMessage("Fetch all orders")
    public ResponseEntity<ResultPaginationDTO> getAllOrder(@Filter Specification<Order> spec,
                                                           Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.orderService.fetchAll(spec, pageable));
    }

    @PutMapping("/orders")
    @ApiMessage("Update status to order")
    public ResponseEntity<Order> updateStatusOrder(@RequestBody Order order) {
        return ResponseEntity.ok().body(this.orderService.handleUpdateStatus(order));
    }

    @GetMapping("orders/export")
    @ApiMessage("Export all orders success")
    public ResponseEntity<String> exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=orders_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Order> listOrders = this.orderService.fetchAllOrder();

        ExportOrder excelExporter = new ExportOrder(listOrders);

        excelExporter.export(response);

        return ResponseEntity.ok().body("ok");
    }
}
