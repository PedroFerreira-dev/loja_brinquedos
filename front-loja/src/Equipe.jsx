import React from 'react';

function SobreEquipe() {
  // Lista de integrantes baseada no layout da página 9 do PDF
  const integrantes = [
    { nome: "Nome do Integrante", rgm: "RGM 000000-0", foto: "https://via.placeholder.com/150" },
    { nome: "Nome do Integrante", rgm: "RGM 000000-0", foto: "https://via.placeholder.com/150" },
    { nome: "Nome do Integrante", rgm: "RGM 000000-0", foto: "https://via.placeholder.com/150" },
    { nome: "Nome do Integrante", rgm: "RGM 000000-0", foto: "https://via.placeholder.com/150" },
    { nome: "Nome do Integrante", rgm: "RGM 000000-0", foto: "https://via.placeholder.com/150" },
    { nome: "Nome do Integrante", rgm: "RGM 000000-0", foto: "https://via.placeholder.com/150" },
  ];

  return (
    <div className="vh-100 d-flex flex-column bg-white">
      {/* Mini-Navegação Superior para voltar à Home */}
      <nav className="p-3 border-bottom d-flex justify-content-between align-items-center">
        <h4 className="fw-bold text-secondary mb-0">ToyBox <span style={{color: '#ff007f'}}>Project</span></h4>
        <button className="btn btn-sm btn-outline-secondary rounded-pill" onClick={() => window.location.href='/'}>Voltar para Loja</button>
      </nav>

      <div className="container py-5">
        <div className="text-center mb-5">
          <h2 className="fw-bold">Sobre a Equipe</h2>
          <p className="text-muted">Desenvolvedores da Loja de Brinquedos ToyBox</p>
        </div>

        <div className="row row-cols-1 row-cols-md-3 g-5">
          {integrantes.map((membro, i) => (
            <div className="col text-center" key={i}>
              <div className="p-4 rounded-4 shadow-sm border h-100">
                <img 
                  src={membro.foto} 
                  className="rounded-circle mb-3 border border-4 border-light shadow-sm" 
                  style={{ width: '130px', height: '130px', objectFit: 'cover' }} 
                  alt="Foto Integrante"
                />
                <h5 className="fw-bold mb-1">{membro.nome}</h5>
                <small className="text-muted">{membro.rgm}</small>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default SobreEquipe;