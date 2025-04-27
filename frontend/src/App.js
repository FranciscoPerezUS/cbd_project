import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import './App.css';

// Componente para mostrar las publicaciones
function Posts() {
  const [posts, setPosts] = useState([]);
  const [likeModal, setLikeModal] = useState({ isOpen: false, postId: null });
  const [likeForm, setLikeForm] = useState({ username: '', password: '' });

  useEffect(() => {
    const fetchPosts = async () => {
      const response = await fetch('http://localhost:8080/posts');
      if (response.ok) {
        const data = await response.json();
        setPosts(data);
      } else {
        alert('Error al obtener las publicaciones');
      }
    };

    fetchPosts();
  }, []);

  const handleInputChange = (e, formSetter) => {
    const { name, value } = e.target;
    formSetter((prev) => ({ ...prev, [name]: value }));
  };

  const handleLike = async (e) => {
    e.preventDefault();
    const { postId } = likeModal;

    const updatedLikeForm = { ...likeForm, postId };

    try {
      const response = await fetch(`http://localhost:8080/posts/like`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedLikeForm),
      });

      if (response.ok) {
        alert('¡Publicación marcada como me gusta!');
        setLikeModal({ isOpen: false, postId: null });
        setLikeForm({ username: '', password: '' });
        const updatedPosts = await fetch('http://localhost:8080/posts').then((res) => res.json());
        setPosts(updatedPosts);
      } else {
        console.error('Failed to like post:', response.status, response.statusText);
        alert('Error al marcar la publicación como me gusta');
      }
    } catch (error) {
      console.error('Error in handleLike:', error);
      alert('Error al marcar la publicación como me gusta');
    }
  };

  return (
    <div className="posts-container">
      <h1>Publicaciones</h1>
      <ul className="posts-list">
        {posts.map((post, index) => (
          <li key={index} className="post-item">
            <h3>{post.title}</h3>
            <p>{post.description}</p>
            <p className="post-meta">Author: {post.name} | Email: {post.email}</p>
            <button
              className="like-button"
              onClick={() => setLikeModal({ isOpen: true, postId: post.id })}
            >
              Me gusta
            </button>
            <span className="likes-count">Likes: {post.likes}</span>
          </li>
        ))}
      </ul>

      {likeModal.isOpen && (
  <div
    className="modal"
    style={{
      position: 'fixed',
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      backgroundColor: 'rgba(0, 0, 0, 0.5)',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      zIndex: 1000,
    }}
  >
    <div
      className="register-form"
      style={{
        width: '100%',
        maxWidth: '400px',
        textAlign: 'center',
        padding: '2rem',
        backgroundColor: 'white',
        borderRadius: 'var(--border-radius)',
        boxShadow: 'var(--box-shadow)',
      }}
    >
      <h2 style={{ marginBottom: '1rem', color: 'var(--primary-color)' }}>Confirmar Me gusta</h2>
      <form onSubmit={handleLike}>
        <label>Nombre de usuario</label>
        <input
          type="text"
          name="username"
          placeholder="Username"
          value={likeForm.username}
          onChange={(e) => handleInputChange(e, setLikeForm)}
          required
        />

        <label>Contraseña</label>
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={likeForm.password}
          onChange={(e) => handleInputChange(e, setLikeForm)}
          required
        />

        <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
          <button type="submit" className="register-button">
            <span className="button-text">Confirmar</span>
          </button>
          <button
            type="button"
            onClick={() => setLikeModal({ isOpen: false, postId: null })}
            className="register-button"
          >
            <span className="button-text">Cancelar</span>
          </button>
        </div>
      </form>
    </div>
  </div>
)}
    </div>
  );
}

// Componente para registrar usuarios
function Register() {
  const [userForm, setUserForm] = useState({ name: '', email: '', username: '', password: '' });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleUserSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch('http://localhost:8080/usuarios', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userForm),
    });

    if (response.ok) {
      alert('¡Usuario registrado con éxito!');
      setUserForm({ name: '', email: '', username: '', password: '' });
    } else {
      alert('Error al registrar el usuario');
    }
  };

  return (
      <div className="register-container">
        <h1>Registrarse</h1>
    <form className="register-form" onSubmit={handleUserSubmit}>
      
      {/* inputs */}
      <label>Nombre</label>
      <input type="text" name="name" placeholder="Name" value={userForm.name} onChange={handleInputChange} required />
      
      <label>Correo</label>
      <input type="email" name="email" placeholder="Email" value={userForm.email} onChange={handleInputChange} required />
      
      <label>Nombre de usuario</label>
      <input type="text" name="username" placeholder="Username" value={userForm.username} onChange={handleInputChange} required />
      
      <label>Contraseña</label>
      <input type="password" name="password" placeholder="Password" value={userForm.password} onChange={handleInputChange} required />
      
      <button className="register-button" type="submit">
        <span className="button-text">Registrarse</span>
      </button>
    </form>
  </div>
  );
}

// Componente para enviar publicaciones
function SubmitPost() {
  const [postForm, setPostForm] = useState({ title: '', description: '', username: '', password: '' });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPostForm((prev) => ({ ...prev, [name]: value }));
  };

  const handlePostSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch('http://localhost:8080/posts', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(postForm),
    });

    if (response.ok) {
      alert('¡Publicación enviada con éxito!');
      setPostForm({ title: '', description: '', username: '', password: '' });
    } else {
      alert('Error al enviar la publicación');
    }
  };

  return (
    <div className="register-container">
      <h1>Crear Post</h1>
      <form className="register-form" onSubmit={handlePostSubmit}>
        <input
          type="text"
          name="title"
          placeholder="Título"
          value={postForm.title}
          onChange={handleInputChange}
          required
        />
        <br /><br />
        <input
          type="text"
          name="description"
          placeholder="Descripción"
          value={postForm.description}
          onChange={handleInputChange}
          required
        />
        <br /><br />
        <input
          type="text"
          name="username"
          placeholder="Nombre de usuario"
          value={postForm.username}
          onChange={handleInputChange}
          required
        />
        <br /><br />
        <input
          type="password"
          name="password"
          placeholder="Contraseña"
          value={postForm.password}
          onChange={handleInputChange}
          required
        />
        <br /><br />
        <button className="register-button" type="submit">
        <span className="button-text">Publicar Post</span>
      </button>
      </form>
    </div>
  );
}

// Componente Home
function Home() {
  return (
    <div className="home-page">
          <h1>Bienvenidos!</h1>
          <div className="navigation-cards">
            <Link to="/posts" className="nav-card">
              <h2>Ver Publicaciones</h2>
              <p>Ve todas las últimas publicaciones</p>
            </Link>
            <Link to="/submit-post" className="nav-card">
              <h2>Crear Publicación</h2>
              <p>Hacer una publicación</p>
            </Link>
            <Link to="/register" className="nav-card">
              <h2>Registrarse</h2>
              <p>Crear una nueva cuenta</p>
            </Link>
          </div>
        </div>
  );
}

// Componente principal App
function App() {
  return (
    <Router>
      <div>
        <nav className="main-nav">
          <Link to="/" className="nav-link">Inicio</Link>
          <Link to="/posts" className="nav-link">Publicaciones</Link>
          <Link to="/register" className="nav-link">Registrar</Link>
          <Link to="/submit-post" className="nav-link">Hacer publicación</Link>
        </nav>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/posts" element={<Posts />} />
          <Route path="/register" element={<Register />} />
          <Route path="/submit-post" element={<SubmitPost />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;