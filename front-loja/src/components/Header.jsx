import React, { useState } from 'react';

function Header({ onSearch, onFilter, carrinho = [], setPagina }) {
  const [filtroAtivo, setFiltroAtivo] = useState('TODOS');
  const [mostrarMegaMenu, setMostrarMegaMenu] = useState(false);

  const totalItens = carrinho.reduce((total, item) => total + item.quantidade, 0);

  const handleFiltro = (categoria) => {
    setFiltroAtivo(categoria);
    onFilter(categoria);
    setMostrarMegaMenu(false); // Fecha o menu após clicar
    if (setPagina) setPagina("loja"); // Garante que volta para a loja se estiver em outra tela
  };

  return (
    <header className="shadow-sm sticky-top bg-white" onMouseLeave={() => setMostrarMegaMenu(false)}>
      {/* Faixa Superior */}
      <div className="text-white text-center py-1 fw-bold" style={{ backgroundColor: '#cc0066', fontSize: '12px' }}>
        FRETE GRÁTIS EM COMPRAS ACIMA DE R$ 199,90! 🚚
      </div>

      <div className="py-3" style={{ backgroundColor: '#ff007f' }}>
        <div className="container d-flex align-items-center justify-content-between gap-3">
          <div 
            className="text-white fw-bold fs-2" 
            style={{ fontFamily: 'cursive', cursor: 'pointer', userSelect: 'none' }} 
            onDoubleClick={() => setPagina("admin")}
          >
            🌞 ToyBox
          </div>

          <div className="flex-grow-1 mx-lg-4" style={{ maxWidth: '600px' }}>
            <div className="input-group shadow-sm rounded-pill overflow-hidden">
              <span className="input-group-text bg-white border-0 text-muted">🔍</span>
              <input 
                type="text" 
                className="form-control border-0 py-2 shadow-none" 
                placeholder="Busque por heróis, bonecas ou jogos..." 
                onChange={(e) => onSearch(e.target.value)} 
              />
            </div>
          </div>

          <div className="d-flex align-items-center text-white gap-4 fs-4">
             <span style={{ cursor: 'pointer' }}>👤</span>
             {/* BOTÃO CARRINHO: Certifique-se que o ID coincida com a sua div de Offcanvas */}
             <div 
                style={{ cursor: 'pointer', position: 'relative' }} 
                data-bs-toggle="offcanvas" 
                data-bs-target="#gavetaCarrinho"
                aria-controls="gavetaCarrinho"
             >
                🛒 {totalItens > 0 && (
                  <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style={{ fontSize: '10px' }}>
                    {totalItens}
                  </span>
                )}
             </div>
          </div>
        </div>
      </div>

      {/* MENU DE NAVEGAÇÃO */}
      <div className="bg-white border-bottom position-relative">
        <div className="container d-flex gap-4 py-2 text-uppercase fw-bold" style={{ fontSize: '13px' }}>
            
            {/* GATILHO DO MEGA MENU */}
            <div 
              style={{ cursor: 'pointer' }} 
              onMouseEnter={() => setMostrarMegaMenu(true)}
            >
              ☰ TODOS OS FILTROS
            </div>

            <span style={{ cursor: 'pointer', color: filtroAtivo === 'TODOS' ? '#ff007f' : '#6c757d' }} onClick={() => handleFiltro('TODOS')}>HOME</span>
            <span style={{ cursor: 'pointer', color: filtroAtivo === 'Heróis' ? '#ff007f' : '#6c757d' }} onClick={() => handleFiltro('Heróis')}>HERÓIS</span>
            <span style={{ cursor: 'pointer', color: filtroAtivo === 'Meninas' ? '#ff007f' : '#6c757d' }} onClick={() => handleFiltro('Meninas')}>MENINAS</span>
            <span style={{ cursor: 'pointer', color: '#dc3545' }} onClick={() => handleFiltro('OFERTAS')}>OFERTAS %</span>
        </div>

        {/* CÓDIGO DO QUADRO DE FILTROS (Que estava faltando) */}
        {mostrarMegaMenu && (
          <div 
            className="position-absolute shadow-lg border rounded-3 bg-white p-4" 
            style={{ top: '100%', left: '10%', width: '600px', zIndex: 1050, display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '20px' }}
            onMouseEnter={() => setMostrarMegaMenu(true)}
          >
            <div>
              <h6 className="fw-bold text-primary mb-3 small">Categorias</h6>
              <ul className="list-unstyled small fw-normal">
                {['Heróis', 'Meninas', 'Bebês', 'Bonecas', 'Ação'].map(cat => (
                  <li key={cat} className="mb-2" style={{ cursor: 'pointer' }} onClick={() => handleFiltro(cat)}>{cat}</li>
                ))}
              </ul>
            </div>
            <div>
              <h6 className="fw-bold text-success mb-3 small">Brincadeiras</h6>
              <ul className="list-unstyled small fw-normal">
                {['Jogos', 'Educativos', 'Lego', 'Puzzle', 'Esportes'].map(cat => (
                  <li key={cat} className="mb-2" style={{ cursor: 'pointer' }} onClick={() => handleFiltro(cat)}>{cat}</li>
                ))}
              </ul>
            </div>
            <div>
              <h6 className="fw-bold text-warning mb-3 small">Especiais</h6>
              <ul className="list-unstyled small fw-normal">
                {['Eletrônicos', 'Colecionáveis', 'Artes', 'Ofertas'].map(cat => (
                  <li key={cat} className="mb-2" style={{ cursor: 'pointer' }} onClick={() => handleFiltro(cat)}>{cat}</li>
                ))}
              </ul>
            </div>
          </div>
        )}
      </div>
    </header>
  );
}
export default Header;