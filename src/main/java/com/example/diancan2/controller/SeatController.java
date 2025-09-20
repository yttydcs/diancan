package com.example.diancan2.controller;

import com.example.diancan2.entity.Seat;
import com.example.diancan2.service.SeatService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Hashtable;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-29
 */
@RestController
@RequestMapping("/api/seat")
public class SeatController {

    @Autowired
    private SeatService seatService;

    // 获取所有座位
    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.list();
    }

    // 根据ID获取座位
    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable Long id) {
        return seatService.getById(id);
    }

    // 新增座位
    @PostMapping
    public boolean addSeat(@RequestBody Seat seat) {
        return seatService.save(seat);
    }

    // 更新座位信息
    @PutMapping
    public boolean updateSeat(@RequestBody Seat seat) {
        return seatService.updateById(seat);
    }

    // 删除座位
    @DeleteMapping("/{id}")
    public boolean deleteSeat(@PathVariable Long id) {
        return seatService.removeById(id);
    }

    // 生成二维码
    @GetMapping("/qrcode/{id}")
    public String generateQRCode(@PathVariable Long id) throws WriterException, IOException {
        Seat seat = seatService.getById(id);
        if (seat == null) {
            return "Seat not found";
        }

        // 这里我假设小程序/H5页面的URL是 "http://your_domain/order?seatId="
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

        // 更新座位信息中的二维码
        String qrCodeBase64 = Base64.getEncoder().encodeToString(pngData);
        seat.setQrCode(qrCodeBase64);
        seatService.updateById(seat);

        return qrCodeBase64;
    }
}
