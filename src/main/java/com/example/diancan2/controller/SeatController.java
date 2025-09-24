package com.example.diancan2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.diancan2.entity.Seat;
import com.example.diancan2.entity.User;
import com.example.diancan2.mapper.UserStoreMapper;
import com.example.diancan2.service.SeatService;
import com.example.diancan2.vo.ApiResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping("/api/seat")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private UserStoreMapper userStoreMapper;

    @GetMapping
    public ApiResponse<List<Seat>> getAllSeats() {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("admin")) {
            return ApiResponse.success(seatService.list());
        } else {
            List<Long> storeIds = userStoreMapper.findStoreIdsByUserId(currentUser.getId());
            if (storeIds == null || storeIds.isEmpty()) {
                return ApiResponse.success(Collections.emptyList());
            }
            return ApiResponse.success(seatService.list(new QueryWrapper<Seat>().in("store_id", storeIds)));
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Seat> getSeatById(@PathVariable Long id) {
        return ApiResponse.success(seatService.getById(id));
    }

    @GetMapping("/customer/{id}")
    public ApiResponse<Seat> getSeatByIdForCustomer(@PathVariable Long id) {
        return ApiResponse.success(seatService.getById(id));
    }

    @PostMapping
    public ApiResponse<Seat> addSeat(@RequestBody Seat seat) {
        seatService.save(seat);
        return ApiResponse.success("座位创建成功", seat);
    }

    @PutMapping
    public ApiResponse<Seat> updateSeat(@RequestBody Seat seat) {
        seatService.updateById(seat);
        return ApiResponse.success("座位更新成功", seat);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSeat(@PathVariable Long id) {
        seatService.removeById(id);
        return ApiResponse.success("座位删除成功", null);
    }

    @GetMapping("/qrcode/{id}")
    public ApiResponse<String> generateQRCode(@PathVariable Long id) throws WriterException, IOException {
        Seat seat = seatService.getById(id);
        if (seat == null) {
            return ApiResponse.error("座位未找到");
        }

        String url = "http://your_domain/order?seatId=" + id;
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        String qrCodeBase64 = Base64.getEncoder().encodeToString(pngData);
        seat.setQrCode(qrCodeBase64);
        seatService.updateById(seat);

        return ApiResponse.success("二维码生成成功", qrCodeBase64);
    }
}
