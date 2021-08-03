package com.moneyroomba.web.rest;

import com.moneyroomba.service.ReportsService;
import com.moneyroomba.service.dto.reports.TransactionsByCategoryDTO;
import com.moneyroomba.service.dto.reports.WalletBalanceReportDTO;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ReportsResource controller
 */
@RestController
@RequestMapping("/api/reports")
public class ReportsResource {

    private final ReportsService reportsService;

    private final Logger log = LoggerFactory.getLogger(ReportsResource.class);

    public ReportsResource(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    /**
     * GET transactionsByCategory
     */

    @GetMapping("/transactions-by-category")
    public ResponseEntity<List<TransactionsByCategoryDTO>> transactionsByCategory() {
        // Se debe implementar el funcionamiento del metodo getTransactionsByCategory
        return ResponseEntity.ok().body(null);
    }

    /**
     * GET transactionsByCategory
     */
    @GetMapping("/transactions-by-category/{id}")
    public ResponseEntity<List<TransactionsByCategoryDTO>> transactionsByCategoryByWallet(@PathVariable Long id) {
        //List<TransactionsByCategoryDTO> report = reportsService.getTransactionsByCategoryByWallet(id);
        return ResponseEntity.ok().body(null);
    }

    /**
     * GET walletBalance
     */
    @GetMapping("/wallet-balance")
    public ResponseEntity<List<WalletBalanceReportDTO>> walletBalance(@RequestParam String startDate, @RequestParam String endDate) {
        List<WalletBalanceReportDTO> report = reportsService.getBalanceAllWallets(startDate, endDate);
        return ResponseEntity.ok().body(report);
    }

    /**
     * GET walletBalance
     */
    @GetMapping("/wallet-balance/{id}")
    public ResponseEntity<List<WalletBalanceReportDTO>> walletBalanceByWallet(@PathVariable Long id) {
        List<WalletBalanceReportDTO> report = reportsService.getBalancebyWallet(id);
        return ResponseEntity.ok().body(report);
    }

    /**
     * GET walletPerformance
     */
    @GetMapping("/wallet-performance")
    public String walletPerformance() {
        return "walletPerformance";
    }

    /**
     * GET walletPerformance
     */
    @GetMapping("/wallet-performance/{id}")
    public String walletPerformanceByWallet(@PathVariable Long id) {
        return "walletPerformance";
    }
}
