import React from 'react';

// 1. O componente agora recebe 'setPagina' para alternar entre Loja e Admin
function Header({ onSearch, onFilter, carrinho = [], removerDoCarrinho, setPagina }) {
  
  const totalItens = carrinho.reduce((total, item) => total + item.quantidade, 0);
  const valorTotal = carrinho.reduce((total, item) => total + (item.preco * item.quantidade), 0);

  return (
    <>
      <header className="shadow-sm sticky-top">
        {/* Faixa de Frete Grátis */}
        <div className="text-white text-center py-1 fw-bold" style={{ backgroundColor: '#cc0066', fontSize: '12px' }}>
          FRETE GRÁTIS EM COMPRAS ACIMA DE R$ 199,90! 🚚
        </div>

        {/* Parte Superior: Logo, Busca e Carrinho */}
        <div className="py-3" style={{ backgroundColor: '#ff007f' }}>
          <div className="container d-flex flex-wrap align-items-center justify-content-between gap-3">
            
            {/* Logo ToyBox: Agora volta para a Loja ao ser clicada */}
            <div 
              className="text-white fw-bold fs-2" 
              style={{ fontFamily: 'cursive', cursor: 'pointer' }} 
              onClick={() => setPagina("loja")}
            >
              🌞 ToyBox
            </div>

            <div className="flex-grow-1 mx-lg-4" style={{ maxWidth: '600px' }}>
              <div className="input-group">
                <span className="input-group-text bg-white border-0 rounded-start-pill text-muted">🔍</span>
                <input
                  type="text"
                  className="form-control border-0 rounded-end-pill py-2"
                  placeholder="Busque por heróis, bonecas ou jogos..."
                  onChange={(e) => onSearch(e.target.value)}
                  style={{ boxShadow: 'none' }}
                />
              </div>
            </div>

            <div className="d-flex align-items-center text-white gap-4 fs-4">
               <span style={{ cursor: 'pointer' }} title="Minha Conta">👤</span>
               
               <div 
                  style={{ cursor: 'pointer', position: 'relative' }} 
                  data-bs-toggle="offcanvas" 
                  data-bs-target="#gavetaCarrinho"
                >
                  🛒
                  {totalItens > 0 && (
                    <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style={{ fontSize: '10px' }}>
                      {totalItens}
                    </span>
                  )}
               </div>
            </div>
          </div>
        </div>

        {/* Menu Principal com Botão Admin */}
        <div className="bg-white d-none d-md-block" style={{ borderBottom: '4px solid', borderImage: 'linear-gradient(90deg, #ff007f, #00ccff, #ffea00, #00cc66) 1' }}>
          <div className="container d-flex justify-content-center align-items-center gap-4 py-2 fw-bold text-secondary" style={{ fontSize: '14px' }}>
            <span style={{ cursor: 'pointer' }} onClick={() => { setPagina("loja"); onFilter("TODOS"); }} className="text-dark">HOME</span>
            <span style={{ cursor: 'pointer' }} onClick={() => onFilter("Heróis")}>HERÓIS</span>
            <span style={{ cursor: 'pointer' }} onClick={() => onFilter("Meninas")}>MENINAS</span>
            <span style={{ cursor: 'pointer' }} onClick={() => onFilter("Bebês")}>BEBÊS</span>
            <span style={{ cursor: 'pointer' }} onClick={() => onFilter("OFERTAS")} className="text-danger">OFERTAS %</span>
            
            {/* Botão de Administração que troca a tela no App.jsx */}
            <button 
              className="btn btn-sm btn-dark rounded-pill px-3 fw-bold ms-3"
              style={{ fontSize: '12px' }}
              onClick={() => setPagina('admin')}
            >
              ⚙️ ADMINISTRAÇÃO
            </button>
          </div>
        </div>
      </header>

      {/* Gaveta do Carrinho (Offcanvas) */}
      <div className="offcanvas offcanvas-end" tabIndex="-1" id="gavetaCarrinho" aria-labelledby="gavetaCarrinhoLabel">
        <div className="offcanvas-header text-white" style={{ backgroundColor: '#ff007f' }}>
          <h5 className="offcanvas-title fw-bold" id="gavetaCarrinhoLabel">Meu Carrinho 🛒</h5>
          <button type="button" className="btn-close btn-close-white" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        
        <div className="offcanvas-body d-flex flex-column">
          {carrinho.length === 0 ? (
            <div className="text-center text-muted mt-5">
              <p className="fs-1">🛍️</p>
              <h5>Sua caixa de brinquedos está vazia!</h5>
            </div>
          ) : (
            <>
              <div className="flex-grow-1 overflow-auto">
                {carrinho.map((item, index) => (
                  <div key={index} className="d-flex align-items-center mb-3 pb-3 border-bottom">
                    <img 
                      src={item.caminhoImagem || "https://via.placeholder.com/50"} 
                      alt={item.nome} 
                      className="rounded"
                      style={{ width: '60px', height: '60px', objectFit: 'contain' }} 
                    />
                    <div className="ms-3 flex-grow-1">
                      <h6 className="mb-0 text-secondary" style={{ fontSize: '14px' }}>{item.nome}</h6>
                      <small className="text-muted">Qtd: {item.quantidade}</small>
                      <br/>
                      <strong style={{ color: '#ff007f' }}>
                        R$ {(item.preco * item.quantidade).toFixed(2).replace('.', ',')}
                      </strong>
                    </div>
                    <button className="btn btn-sm btn-outline-danger ms-2" onClick={() => removerDoCarrinho(item.id)}>
                      🗑️
                    </button>
                  </div>
                ))}
              </div>

              <div className="mt-3 pt-3 border-top">
                <div className="d-flex justify-content-between align-items-center mb-3">
                  <span className="fs-5 text-muted">Total:</span>
                  <span className="fs-4 fw-bold" style={{ color: '#ff007f' }}>
                    R$ {valorTotal.toFixed(2).replace('.', ',')}
                  </span>
                </div>
                <button className="btn w-100 fw-bold text-white rounded-pill py-2" style={{ backgroundColor: '#00cc66' }}>
                  Finalizar Compra Segura 🔒
                </button>
              </div>
            </>
          )}
        </div>
      </div>
    </>
  );
}

export default Header;