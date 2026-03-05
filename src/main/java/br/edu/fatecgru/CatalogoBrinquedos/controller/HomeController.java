package br.edu.fatecgru.CatalogoBrinquedos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.edu.fatecgru.CatalogoBrinquedos.dto.BrinquedoRequestDTO;
import br.edu.fatecgru.CatalogoBrinquedos.service.BrinquedoService;

@Controller // Indica ao Spring que esta classe gerencia as telas e URLs
public class HomeController {

    @Autowired // O Spring injeta automaticamente o nosso Service (regras de negócio) aqui *Injeção de dependência
    private BrinquedoService service; 

    // ROTA GET: Acionada ao acessar "localhost:8080/". Prepara e carrega a tela.
    @GetMapping("/")
    public String abrirTelaInicial(Model model) {
        
        // 1. Envia o DTO vazio para o formulário HTML preencher os dados
        model.addAttribute("novoBrinquedo", new BrinquedoRequestDTO());
        
        // 2. Busca a lista de brinquedos no Service e envia para a tabela
        model.addAttribute("brinquedos", service.listarTodos());
        
        return "index"; // Renderiza o arquivo "index.html" da pasta templates
    }

    // ROTA POST: Acionada quando o usuário clica em "Salvar" no formulário
    @PostMapping("/salvar")
    public String salvarBrinquedo(@ModelAttribute BrinquedoRequestDTO request) {
        
        // O @ModelAttribute empacota os dados da tela. Repassamos para o Service salvar no banco.
        service.salvarBrinquedo(request);
        
        // Redireciona para a rota "/" para recarregar a página e atualizar a tabela
        return "redirect:/";
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        service.excluirBrinquedo(id);
        return "redirect:/"; // Volta para a tela inicial atualizada já sem o brinquedo excluído. Nesse caso é um public string
        //porque o método precisa devolver uma resposta, e a resposta é a rota para onde o usuário deve ser redirecionado após 
        //clicar no botão excluir. O redirect é necessário para recarregar a página e atualizar a tabela sem o brinquedo excluído.
        //Se fosse um void, o método não poderia devolver a rota para redirecionamento, e o usuário ficaria na mesma página sem 
        //perceber que o brinquedo foi excluído.
    }
    
    @GetMapping("/editar/{id}")
    public String prepararEdicao(@PathVariable Long id, Model model) {
        // 1. Busca os dados do brinquedo
        BrinquedoRequestDTO brinquedoParaEditar = service.buscarPorId(id);
        
        // 2. Coloca os dados no formulário (substituindo o DTO vazio)
        model.addAttribute("novoBrinquedo", brinquedoParaEditar);
        
        // 3. Recarrega a lista para a tabela não sumir
        model.addAttribute("brinquedos", service.listarTodos());
        
        return "index";
    }
    
}