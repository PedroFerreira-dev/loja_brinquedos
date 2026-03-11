import { useState, useEffect } from 'react';
import Header from './components/Header';
import Banner from './components/Banner';
import Footer from './components/Footer';
import Admin from './Admin'; 
import LoginAdmin from './Login'; // Mudei de "./LoginAdmin" para "./login" para bater com seu arquivo
import Swal from 'sweetalert2';

function App() {
  const [estoqueCompleto, setEstoqueCompleto] = useState([]);
  const [brinquedosVisiveis, setBrinquedosVisiveis] = useState([]);
  const [carrinho, setCarrinho] = useState([]);
  const [telaAtiva, setTelaAtiva] = useState('loja');
  
  const [logado, setLogado] = useState(false);
  const [credenciais, setCredenciais] = useState(null);

  const carregarBrinquedos = (termo = "") => {
    const url = termo 
      ? `http://localhost:8080/api/brinquedos/buscar?termo=${termo}`
      : "http://localhost:8080/api/brinquedos";

    fetch(url)
      .then(res => res.json())
      .then(dados => {
        setEstoqueCompleto(dados);
        setBrinquedosVisiveis(dados);
      })
      .catch(err => console.error("Erro na integração:", err));
  };

  useEffect(() => {
    carregarBrinquedos();
  }, []);

  // FUNÇÃO DE LOGIN - Teste com admin / admin123
  const realizarLogin = (usuarioDigitado, senhaDigitada) => {
    const u = usuarioDigitado.trim();
    const s = senhaDigitada.trim();

    if (u === 'admin' && s === 'admin123') {
      setCredenciais({ username: u, password: s });
      setLogado(true);
      Swal.fire('Sucesso!', 'Acesso administrativo liberado.', 'success');
    } else {
      Swal.fire('Erro', 'Usuário ou senha inválidos. Tente admin / admin123', 'error');
    }
  };

  const handleSairAdmin = () => {
    setLogado(false);
    setCredenciais(null);
    setTelaAtiva('loja');
  };

  // ... restante do código (filtrarPorCategoria, adicionarAoCarrinho e return)
  // Certifique-se de manter o return que usa <LoginAdmin onLogin={realizarLogin} />

  const filtrarPorCategoria = (categoria) => {
    if (categoria === "TODOS") setBrinquedosVisiveis(estoqueCompleto);
    else {
      const filtrados = estoqueCompleto.filter(b => 
        b.categoria.toUpperCase() === categoria.toUpperCase()
      );
      setBrinquedosVisiveis(filtrados);
    }
  };

  const adicionarAoCarrinho = (brinquedo) => {
    setCarrinho((atual) => {
      const existe = atual.find(item => item.id === brinquedo.id);
      if (existe) return atual.map(item => item.id === brinquedo.id ? { ...item, quantidade: item.quantidade + 1 } : item);
      return [...atual, { ...brinquedo, quantidade: 1 }];
    });
    Swal.fire({ title: `${brinquedo.nome}`, text: "Adicionado! 🛒", icon: 'success', toast: true, position: 'top-end', showConfirmButton: false, timer: 2000 });
  };

  return (
    <div className="min-vh-100 bg-light">
      <Header 
        onSearch={carregarBrinquedos} 
        onFilter={filtrarPorCategoria} 
        carrinho={carrinho}
        removerDoCarrinho={(id) => setCarrinho(carrinho.filter(i => i.id !== id))}
        setPagina={setTelaAtiva} 
      />

      {telaAtiva === 'loja' ? (
        <>
          <Banner />
          <div className="container py-4">
            <h2 className="mb-4 fw-bold text-secondary text-center">
              Mais Vendidos na <span style={{ color: '#ff007f' }}>ToyBox</span> 🧸
            </h2>
            <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
              {brinquedosVisiveis.map(b => (
                <CardBrinquedo key={b.id} brinquedo={b} adicionarAoCarrinho={adicionarAoCarrinho} />
              ))}
            </div>
          </div>
        </>
      ) : (
        !logado ? (
          <LoginAdmin onLogin={realizarLogin} />
        ) : (
          <Admin 
            voltarParaLoja={handleSairAdmin} 
            authData={credenciais} 
          />
        )
      )}

      <Footer />
    </div>
  );
}

function CardBrinquedo({ brinquedo, adicionarAoCarrinho }) {
  const urlImagem = brinquedo.caminhoImagem || "https://via.placeholder.com/300";
  return (
    <div className="col">
      <div className="card h-100 border-0 shadow-sm rounded-4 overflow-hidden">
        <div className="d-flex justify-content-center p-3" style={{ height: '200px' }}>
          <img src={urlImagem} alt={brinquedo.nome} className="img-fluid" style={{ objectFit: 'contain' }} />
        </div>
        <div className="card-body d-flex flex-column">
          <span className="badge mb-2 bg-info">{brinquedo.categoria}</span>
          <h6 className="card-title text-secondary">{brinquedo.nome}</h6>
          <div className="mt-auto text-center">
            <p className="fs-4 fw-bold mb-1" style={{ color: '#ff007f' }}>R$ {brinquedo.preco.toFixed(2).replace('.', ',')}</p>
            <button className="btn btn-success w-100 rounded-pill fw-bold" onClick={() => adicionarAoCarrinho(brinquedo)}>Adicionar 🛒</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;