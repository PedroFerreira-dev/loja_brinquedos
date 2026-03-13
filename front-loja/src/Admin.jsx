import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';

function Admin({ voltarParaLoja, authData }) {
  const [brinquedos, setBrinquedos] = useState([]);
  const [termoBusca, setTermoBusca] = useState('');
  const [modo, setModo] = useState('listagem'); 
  const [editandoId, setEditandoId] = useState(null);
  const [categoriasExistentes, setCategoriasExistentes] = useState([]);
  const [marcasExistentes, setMarcasExistentes] = useState([]);

  const [formData, setFormData] = useState({
    nome: '', marca: '', categoria: '', preco: 0, 
    caminhoImagem: '', descricao: ''
  });

  const urlApi = "http://localhost:8080/api/brinquedos";

  // Função de listagem principal
  const listar = async () => {
    try {
      const res = await axios.get(urlApi, { auth: authData });
      setBrinquedos(res.data);
    } catch (err) { console.error("Erro ao listar", err); }
  };

  // Carregar sugestões para os datalists (Resolvendo Erro 400)
  const carregarSugestoes = async () => {
    if (!authData) return;
    try {
      const config = { auth: authData };
      const [resCat, resMar] = await Promise.all([
        axios.get(`${urlApi}/categorias`, config),
        axios.get(`${urlApi}/marcas`, config)
      ]);
      setCategoriasExistentes(resCat.data || []);
      setMarcasExistentes(resMar.data || []);
    } catch (err) { 
      console.error("Erro 400: Verifique se a URL /marcas existe no Java", err); 
    }
  };

  useEffect(() => { listar(); }, []);

  useEffect(() => {
    if (modo !== 'listagem') carregarSugestoes();
  }, [modo]);

  const salvar = async (e) => {
    e.preventDefault();
    const dados = { ...formData, preco: Number(formData.preco) };
    try {
      if (modo === 'editar') {
        await axios.put(`${urlApi}/${editandoId}`, dados, { auth: authData });
      } else {
        await axios.post(urlApi, dados, { auth: authData });
      }
      Swal.fire('Sucesso!', 'Dados gravados com sucesso.', 'success');
      setModo('listagem');
      listar();
    } catch (err) { Swal.fire('Erro', 'Falha ao salvar.', 'error'); }
  };

  const excluir = (id) => {
    Swal.fire({
      title: 'Confirma a exclusão?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'SIM',
      cancelButtonText: 'NÃO'
    }).then(async (result) => {
      if (result.isConfirmed) {
        await axios.delete(`${urlApi}/${id}`, { auth: authData });
        listar();
      }
    });
  };

  const brinquedosFiltrados = brinquedos.filter(b => 
    b.nome?.toLowerCase().includes(termoBusca.toLowerCase()) ||
    b.categoria?.toLowerCase().includes(termoBusca.toLowerCase()) ||
    b.marca?.toLowerCase().includes(termoBusca.toLowerCase())
  );

  // ESTRUTURA DE LAYOUT UNIFICADA (Resolve Sidebar Sumindo)
  return (
    <div className="d-flex" style={{ minHeight: '100vh', backgroundColor: '#f4f7f6' }}>
      {/* SIDEBAR FIXA */}
      <div className="bg-dark text-white p-4 shadow" style={{ width: '280px', position: 'fixed', height: '100vh' }}>
        <h4 className="fw-bold mb-4 border-bottom pb-2 text-center">ToyBox Admin</h4>
        <ul className="nav flex-column gap-2">
          <li className={`nav-item btn ${modo === 'listagem' ? 'btn-primary' : 'btn-outline-light'} text-start border-0`} onClick={() => setModo('listagem')}>
            📦 Ver Catálogo
          </li>
          <li className={`nav-item btn ${modo === 'novo' ? 'btn-primary' : 'btn-outline-light'} text-start border-0`} onClick={() => { setModo('novo'); setFormData({nome:'', marca:'', categoria:'', preco:0, caminhoImagem:'', descricao:''}); }}>
            ➕ Novo Brinquedo
          </li>
          <li className="nav-item btn btn-danger mt-5 rounded-pill fw-bold" onClick={voltarParaLoja}>
            Sair do Painel
          </li>
        </ul>
      </div>

      {/* CONTEÚDO DINÂMICO (Listagem ou Formulário) */}
      <div className="flex-grow-1" style={{ marginLeft: '280px', padding: '40px' }}>
        
        {modo === 'listagem' ? (
          <>
            <div className="d-flex justify-content-between align-items-center mb-4">
              <h2 className="text-secondary fw-bold fs-4">Painel de Controle :: Catálogo</h2>
              <div className="input-group" style={{ maxWidth: '350px' }}>
                <span className="input-group-text bg-white border-end-0">🔍</span>
                <input type="text" className="form-control border-start-0 shadow-none" placeholder="Buscar..." value={termoBusca} onChange={(e) => setTermoBusca(e.target.value)} />
              </div>
            </div>
            <div className="card shadow-sm border-0">
              <table className="table table-hover align-middle mb-0">
                <thead className="table-light">
                  <tr><th>Nome</th><th>Marca</th><th>Categoria</th><th>Valor</th><th className="text-center">Ações</th></tr>
                </thead>
                <tbody>
                  {brinquedosFiltrados.map(b => (
                    <tr key={b.id}>
                      <td><small className="text-muted me-1">#{b.id}</small> {b.nome}</td>
                      <td>{b.marca}</td>
                      <td><span className="badge bg-secondary">{b.categoria}</span></td>
                      <td>R$ {Number(b.preco).toFixed(2)}</td>
                      <td className="text-center">
                        <button className="btn btn-sm btn-info me-2 text-white" onClick={() => { setEditandoId(b.id); setFormData(b); setModo('editar'); }}>Editar</button>
                        <button className="btn btn-sm btn-danger" onClick={() => excluir(b.id)}>Excluir</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        ) : (
          /* FORMULÁRIO CENTRALIZADO */
          <div className="d-flex justify-content-center">
            <div className="card shadow-lg border-0 w-100" style={{ maxWidth: '700px' }}>
              <div className="card-header bg-dark text-white p-3 text-center">
                <h4 className="mb-0">{modo === 'novo' ? 'Novo Cadastro' : 'Editar Produto'}</h4>
              </div>
              <form onSubmit={salvar} className="card-body p-4">
                <div className="row g-3">
                  <div className="col-md-6">
                    <label className="fw-bold small">CÓDIGO:</label>
                    <input className="form-control bg-light" value={modo === 'editar' ? editandoId : 'AUTOMÁTICO'} readOnly />
                  </div>
                  <div className="col-md-6">
                    <label className="fw-bold small">MARCA:</label>
                    <input list="sugestoesMarcas" className="form-control" value={formData.marca} onChange={e => setFormData({...formData, marca: e.target.value})} placeholder="Selecione ou digite..." />
                    <datalist id="sugestoesMarcas">
                      {marcasExistentes.map((m, i) => <option key={i} value={m} />)}
                    </datalist>
                  </div>
                  <div className="col-12">
                    <label className="fw-bold small">NOME DO BRINQUEDO:</label>
                    <input className="form-control" required value={formData.nome} onChange={e => setFormData({...formData, nome: e.target.value})} />
                  </div>
                  <div className="col-md-6">
                    <label className="fw-bold small">CATEGORIA:</label>
                    <input list="sugestoesCategorias" className="form-control" value={formData.categoria} onChange={e => setFormData({...formData, categoria: e.target.value})} placeholder="Selecione ou digite..." />
                    <datalist id="sugestoesCategorias">
                      {categoriasExistentes.map((c, i) => <option key={i} value={c} />)}
                    </datalist>
                  </div>
                  <div className="col-md-6">
                    <label className="fw-bold small">VALOR (R$):</label>
                    <input type="number" step="0.01" className="form-control" value={formData.preco} onChange={e => setFormData({...formData, preco: e.target.value})} />
                  </div>
                  <div className="col-12">
                    <label className="fw-bold small">URL DA IMAGEM:</label>
                    <input className="form-control" value={formData.caminhoImagem} onChange={e => setFormData({...formData, caminhoImagem: e.target.value})} />
                  </div>
                  <div className="col-12">
                    <label className="fw-bold small">DESCRIÇÃO:</label>
                    <textarea className="form-control" rows="2" value={formData.descricao} onChange={e => setFormData({...formData, descricao: e.target.value})}></textarea>
                  </div>
                </div>
                <div className="mt-4 d-flex gap-2">
                  <button type="submit" className="btn btn-success px-5 fw-bold">SALVAR</button>
                  <button type="button" className="btn btn-secondary" onClick={() => setModo('listagem')}>CANCELAR</button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Admin;