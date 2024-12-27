package com.smartParking.service.impl;
import com.smartParking.dao.ParkingLotDAO;
import com.smartParking.dao.impl.LotManagerDAOImpl;
import com.smartParking.dao.impl.TopUserDAOImpl;
import com.smartParking.model.LotManager;
import com.smartParking.model.ParkingLot;
import com.smartParking.model.TopUser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void lotsMangerJasperReport(int id) throws JRException {
        JasperReport jasperReport = JasperCompileManager.compileReport("src//main//resources//Reports//lot_managers_report.jrxml");
        List<LotManager> lots = lotManagerDAOImpl.getLots(id) ;
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lots);

        // Parameters (optional)
        HashMap<String, Object> parameters = new HashMap<>();

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export the report to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, "lots_manger_report.pdf");
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
