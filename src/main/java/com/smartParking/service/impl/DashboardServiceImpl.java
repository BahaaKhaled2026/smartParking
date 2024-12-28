package com.smartParking.service.impl;
import com.smartParking.dao.ParkingLotDAO;
import com.smartParking.dao.UserDAO;
import com.smartParking.dao.impl.LotManagerDAOImpl;
import com.smartParking.dao.impl.TopUserDAOImpl;
import com.smartParking.model.LotManager;
import com.smartParking.model.ParkingLot;
import com.smartParking.model.TopUser;
import com.smartParking.model.User;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

@Service
public class DashboardServiceImpl {

    @Autowired
    private TopUserDAOImpl topUserDAOImpl ;

    @Autowired
    private LotManagerDAOImpl lotManagerDAOImpl ;

    @Autowired
    private ParkingLotDAO parkingLotDAO ;

    @Autowired
    private UserDAO userDAO;

    public void topUsersJasperReport() throws JRException {
        JasperReport jasperReport = JasperCompileManager.compileReport("src//main//resources//Reports//top_users_report.jrxml");
        List<TopUser> topUsers = topUserDAOImpl.getTopUsers() ;
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topUsers);

        // Parameters (optional)
        HashMap<String, Object> parameters = new HashMap<>();

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, "top_users_report.pdf");
    }

    public ResponseEntity<byte[]> lotsMangerJasperReport(int id) throws JRException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.getUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if(!user.getRole().equals("MANAGER")){
            throw new IllegalStateException("Only managers can make reports.");
        }

        // Compile the Jasper report

        JasperReport jasperReport = JasperCompileManager.compileReport("src//main//resources//Reports//lot_managers_report.jrxml");
        LotManager lot = lotManagerDAOImpl.getLotById(id);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of(lot));

        // Parameters (optional)
        HashMap<String, Object> parameters = new HashMap<>();

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

        // Return the PDF file as a ResponseEntity
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lot_" + id + "_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(outputStream.toByteArray());
    }

    public void lotsJasperReport() throws JRException {
        JasperReport jasperReport = JasperCompileManager.compileReport("src//main//resources//Reports//lots_report.jrxml");
        List<ParkingLot> lots = parkingLotDAO.getAllLotsOrderedByRevenue() ;
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lots);

        // Parameters (optional)
        HashMap<String, Object> parameters = new HashMap<>();

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, "lots_report.pdf");
    }
}
