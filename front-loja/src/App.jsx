import { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import Header from './components/Header';
import Banner from './components/Banner';
import Footer from './components/Footer';
import Admin from './Admin'; 
import LoginAdmin from './Login'; 
import SobreEquipe from './Equipe'; 
import Detalhes from './Detalhes';
import Swal from 'sweetalert2';

// Componente Interno da Vitrine para usar o hook useNavigate
function Vitrine({ brinquedos, adicionarAoCarrinho }) {
  const navigate = useNavigate();

  return (
    <div className="row row-cols-1 row-cols-md-4 g-4">
      {brinquedos.map(b => (
        <div key={b.id} className="col">
          <div 
            className="card h-100 border-0 shadow-sm rounded-4 p-3 text-center" 
            style={{ cursor: 'pointer' }}
            onClick={() => navigate(`/produto/${b.id}`)}
          >
            <img src={b.caminhoImagem} alt={b.nome} className="img-fluid mb-2" style={{height: '150px', objectFit: 'contain'}} />
            <h6 className="text-secondary small">{b.nome}</h6>
            <p className="fw-bold fs-5" style={{color: '#ff007f'}}>R$ {Number(b.preco).toFixed(2)}</p>
            <button 
              className="btn btn-success btn-sm rounded-pill fw-bold" 
              onClick={(e) => { e.stopPropagation(); adicionarAoCarrinho(b); }}
            >
              Adicionar 🛒
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}

function App() {
  const [estoqueCompleto, setEstoqueCompleto] = useState([]);
  const [brinquedosVisiveis, setBrinquedosVisiveis] = useState([]);
  const [carrinho, setCarrinho] = useState([]);
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

  useEffect(() => { carregarBrinquedos(); }, []);

  const filtrarPorCategoria = (categoria) => {
    if (categoria === "TODOS") setBrinquedosVisiveis(estoqueCompleto);
    else {
      const filtrados = estoqueCompleto.filter(b => b.categoria.toUpperCase() === categoria.toUpperCase());
      setBrinquedosVisiveis(filtrados);
    }
  };

  const adicionarAoCarrinho = (brinquedo) => {
    setCarrinho((atual) => {
      const existe = atual.find(item => item.id === brinquedo.id);
      if (existe) return atual.map(item => item.id === brinquedo.id ? { ...item, quantidade: item.quantidade + 1 } : item);
      return [...atual, { ...brinquedo, quantidade: 1 }];
    });
    Swal.fire({ title: 'Adicionado!', icon: 'success', toast: true, position: 'top-end', showConfirmButton: false, timer: 2000 });
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={
          <div className="loja-layout min-vh-100 bg-light">
            <Header onSearch={carregarBrinquedos} onFilter={filtrarPorCategoria} carrinho={carrinho} />
            <Banner />
            <div className="container py-5">
              <h2 className="mb-4 fw-bold text-secondary text-center">
                Brinquedos em <span style={{ color: '#ff007f' }}>Destaque</span> 🧸
              </h2>
              <Vitrine brinquedos={brinquedosVisiveis} adicionarAoCarrinho={adicionarAoCarrinho} />
            </div>
            <Footer />
          </div>
        } />

        <Route path="/produto/:id" element={
          <Detalhes 
            adicionarAoCarrinho={adicionarAoCarrinho} 
            carrinho={carrinho} 
            onSearch={carregarBrinquedos} 
            onFilter={filtrarPorCategoria} 
          />
        } />
        
        <Route path="/equipe" element={<SobreEquipe />} />
        
        <Route path="/admin" element={
          !logado ? (
            <LoginAdmin onLogin={(u, s) => {
              if (u === 'admin' && s === 'admin123') {
                setCredenciais({ username: u, password: s });
                setLogado(true);
              } else { Swal.fire('Erro', 'Dados inválidos', 'error'); }
            }} />
          ) : (
            <Admin authData={credenciais} voltarParaLoja={() => setLogado(false)} />
          )
        } />
      </Routes>
    </Router>
  );
}

export default App;