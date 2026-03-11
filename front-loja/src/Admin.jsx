import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';

function Admin({ voltarParaLoja, authData }) { // authData vem do login bem-sucedido no App.jsx
  const [brinquedos, setBrinquedos] = useState([]);
  const [exibirFormulario, setExibirFormulario] = useState(false);
  const [editando, setEditando] = useState(null);

  const [formData, setFormData] = useState({
    nome: '', preco: 0.0, categoria: '', caminhoImagem: '', 
    desconto: 0.0, quantidade: 0, vendas: 0, descricao: ''
  });

  const urlApi = "http://localhost:8080/api/brinquedos";

  // IMPORTANTE: Criamos a configuração de autenticação para o Axios
  const authConfig = { 
    auth: authData // Pega o {username, password} que você digitou na tela de login
  };

  const listarBrinquedos = async () => {
    try {
      // Enviamos o authConfig aqui, senão o Java bloqueia a requisição (Erro 401)
      const res = await axios.get(urlApi, authConfig);
      setBrinquedos(res.data);
    } catch (err) {
      console.error("Erro ao carregar lista admin:", err);
      if (err.response?.status === 401) {
        Swal.fire('Sessão Inválida', 'Suas credenciais não foram aceitas pelo servidor.', 'error');
      }
    }
  };

  useEffect(() => {
    // Só tenta listar se tiver dados de autenticação
    if (authData) {
      listarBrinquedos();
    }
  }, [authData]);

  const salvarBrinquedo = async (e) => {
    e.preventDefault();
    try {
      const dadosParaEnviar = {
        ...formData,
        preco: Number(formData.preco) || 0,
        desconto: Number(formData.desconto) || 0,
        quantidade: Number(formData.quantidade) || 0,
        vendas: Number(formData.vendas) || 0
      };

      if (editando) {
        await axios.put(`${urlApi}/${editando}`, dadosParaEnviar, authConfig);
        Swal.fire('Atualizado!', 'Brinquedo editado com sucesso.', 'success');
      } else {
        await axios.post(urlApi, dadosParaEnviar, authConfig);
        Swal.fire('Salvo!', 'Novo brinquedo cadastrado.', 'success');
      }
      
      setExibirFormulario(false);
      setEditando(null);
      listarBrinquedos();
    } catch (err) {
      Swal.fire('Erro', 'Falha ao salvar. Verifique a conexão com o banco.', 'error');
    }
  };

  const confirmarExclusao = (id) => {
    Swal.fire({
      title: 'Confirma a exclusão?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sim, excluir',
      cancelButtonText: 'Cancelar'
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await axios.delete(`${urlApi}/${id}`, authConfig);
          listarBrinquedos();
          Swal.fire('Excluído!', 'O item foi removido.', 'success');
        } catch (err) {
          Swal.fire('Erro', 'Não foi possível excluir.', 'error');
        }
      }
    });
  };

  const prepararEdicao = (b) => {
    setEditando(b.id);
    setFormData(b);
    setExibirFormulario(true);
  };

  // Se estiver em modo formulário, renderiza os inputs
  if (exibirFormulario) {
    return (
      <div className="container py-5">
        <div className="card shadow border-0 p-4">
          <h4 className="mb-4">{editando ? 'Editar Brinquedo' : 'Cadastrar Novo Brinquedo'}</h4>
          <form onSubmit={salvarBrinquedo} className="row g-3">
            <div className="col-md-6">
              <label className="fw-bold">Nome:</label>
              <input type="text" className="form-control" value={formData.nome} onChange={e => setFormData({...formData, nome: e.target.value})} required />
            </div>
            <div className="col-md-3">
              <label className="fw-bold">Preço:</label>
              <input type="number" step="0.01" className="form-control" value={formData.preco} onChange={e => setFormData({...formData, preco: e.target.value})} />
            </div>
            <div className="col-md-3">
              <label className="fw-bold">Quantidade:</label>
              <input type="number" className="form-control" value={formData.quantidade} onChange={e => setFormData({...formData, quantidade: e.target.value})} />
            </div>
            <div className="col-12 mt-4">
              <button type="submit" className="btn btn-success me-2 fw-bold">SALVAR</button>
              <button type="button" className="btn btn-secondary" onClick={() => setExibirFormulario(false)}>CANCELAR</button>
            </div>
          </form>
        </div>
      </div>
    );
  }

  // Tabela Principal
  return (
    <div className="container py-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <button className="btn btn-outline-danger fw-bold" onClick={voltarParaLoja}>⬅ VOLTAR</button>
        <h2 className="text-secondary fw-bold">Painel Administrativo</h2>
        <button className="btn btn-dark fw-bold" onClick={() => {setEditando(null); setFormData({nome:'', preco:0, categoria:'', caminhoImagem:'', desconto:0, quantidade:0, vendas:0, descricao:''}); setExibirFormulario(true);}}>
          + NOVO PRODUTO
        </button>
      </div>

      <div className="card shadow-sm border-0">
        <table className="table table-hover align-middle mb-0">
          <thead className="table-dark">
            <tr>
              <th className="ps-4">Nome</th>
              <th>Preço</th>
              <th>Estoque</th>
              <th className="text-center">Ações</th>
            </tr>
          </thead>
          <tbody>
            {brinquedos.length > 0 ? (
              brinquedos.map(b => (
                <tr key={b.id}>
                  <td className="ps-4 fw-bold">{b.nome}</td>
                  <td>R$ {Number(b.preco).toFixed(2).replace('.', ',')}</td>
                  <td>{b.quantidade} unidades</td>
                  <td className="text-center">
                    <button className="btn btn-sm btn-outline-primary me-2" onClick={() => prepararEdicao(b)}>Editar</button>
                    <button className="btn btn-sm btn-outline-danger" onClick={() => confirmarExclusao(b.id)}>Excluir</button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4" className="text-center py-4 text-muted">Nenhum brinquedo encontrado no banco de dados.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default Admin;