package com.menegheti.teste_estoque.controllers; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.menegheti.teste_estoque.model.DTO.DashboardResumoDTO;
import com.menegheti.teste_estoque.model.DTO.ProdutoDTO;
import com.menegheti.teste_estoque.services.RelatorioService;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")  // Permite chamadas do frontend
public class RelatorioDashboardDTOController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResumoDTO> dashboard() { 
        DashboardResumoDTO dto = relatorioService.gerarDashboard();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/dashboard-basico")
    // @PreAuthorize("hasAnyRole('ADMIN', 'ESTOQUISTA')")
    public ResponseEntity<DashboardResumoDTO> dashboardBasico(
            @RequestParam(required = false, defaultValue = "2023") int ano,
            @RequestParam(required = false, defaultValue = "10") int mes) {
        
        DashboardResumoDTO dto = relatorioService.gerarDashboard(); 
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/produtos-criticos")
    // @PreAuthorize("hasAnyRole('ADMIN', 'ESTOQUISTA')")
    public ResponseEntity<List<ProdutoDTO>> produtosCriticos() {
        // Implemente no service: return relatorioService.listarProdutosCriticos(PageRequest.of(0, 10));
        return ResponseEntity.ok(List.of());
    }
}
