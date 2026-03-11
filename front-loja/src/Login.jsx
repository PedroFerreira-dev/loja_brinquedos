import React, { useState } from 'react';
import Swal from 'sweetalert2';

function LoginAdmin({ onLogin }) {
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');

const manejarLogin = (e) => {
    e.preventDefault();
    // O .trim() remove espaços acidentais (ex: "admin " vira "admin")
    onLogin(usuario.trim(), senha.trim()); 
  };

  return (
    <div className="container d-flex justify-content-center align-items-center" style={{ minHeight: '60vh' }}>
      <div className="card shadow-lg p-4 rounded-4 border-0" style={{ maxWidth: '400px', width: '100%' }}>
        <div className="text-center mb-4">
          <h2 className="fw-bold text-secondary">Acesso <span style={{color: '#ff007f'}}>Restrito</span></h2>
          <p className="text-muted">Área exclusiva para administradores ToyBox</p>
        </div>
        <form onSubmit={manejarLogin}>
          <div className="mb-3">
            <label className="form-label fw-bold">Usuário:</label>
            <input 
              type="text" 
              className="form-control rounded-pill px-3" 
              value={usuario} 
              onChange={(e) => setUsuario(e.target.value)}
              required 
            />
          </div>
          <div className="mb-3">
            <label className="form-label fw-bold">Senha:</label>
            <input 
              type="password" 
              className="form-control rounded-pill px-3" 
              value={senha} 
              onChange={(e) => setSenha(e.target.value)}
              required 
            />
          </div>
          <button type="submit" className="btn btn-dark w-100 rounded-pill py-2 fw-bold mt-3">
            ENTRAR NO SISTEMA 🔐
          </button>
        </form>
      </div>
    </div>
  );
}

export default LoginAdmin;