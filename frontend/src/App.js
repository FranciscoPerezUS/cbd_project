import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import './App.css';

function Home() {
  const [posts, setPosts] = useState([]);
  const [userForm, setUserForm] = useState({ name: '', email: '', username: '', password: '' });
  const [postForm, setPostForm] = useState({ title: '', description: '', username: '', password: '' });
  const [likeModal, setLikeModal] = useState({ isOpen: false, postId: null });
  const [likeForm, setLikeForm] = useState({username: '', password: '' });

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

  const handleUserSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch('http://localhost:8080/usuarios', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userForm)
    });

    if (response.ok) {
      alert('¡Usuario registrado con éxito!');
      setUserForm({ name: '', email: '', username: '', password: '' });
    } else {
      alert('Error al registrar el usuario');
    }
  };

  const handlePostSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch('http://localhost:8080/posts', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(postForm)
    });

    if (response.ok) {
      alert('¡Publicación enviada con éxito!');
      setPostForm({ title: '', description: '', username: '', password: '' });
      const updatedPosts = await fetch('http://localhost:8080/posts').then((res) => res.json());
      setPosts(updatedPosts);
    } else {
      alert('Error al enviar la publicación');
    }
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
    <div style={{ display: 'flex', padding: '2rem' }}>
      <div style={{ width: '30%', marginRight: '2rem' }}>
        <h2>Publicaciones</h2>
        <ul>
          {posts.map((post, index) => (
            <li key={index}>
              <h4>{post.title}</h4>
              <p>{post.description}</p>
              <p>Nombre: {post.name} Correo: {post.email}</p>
              <button onClick={() => setLikeModal({ isOpen: true, postId: post.id })}>Me gusta</button> Likes: {post.likes}
            </li>
          ))}
        </ul>
      </div>
      <div>
        <h1>Register</h1>
        <form onSubmit={handleUserSubmit}>
          <input
            type="text"
            name="name"
            placeholder="Name"
            value={userForm.name}
            onChange={(e) => handleInputChange(e, setUserForm)}
            required
          />
          <br /><br />
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={userForm.email}
            onChange={(e) => handleInputChange(e, setUserForm)}
            required
          />
          <br /><br />
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={userForm.username}
            onChange={(e) => handleInputChange(e, setUserForm)}
            required
          />
          <br /><br />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={userForm.password}
            onChange={(e) => handleInputChange(e, setUserForm)}
            required
          />
          <br /><br />
          <button type="submit">Register</button>
        </form>
      </div>
      <div>
        <h1>Submit Post</h1>
        <form onSubmit={handlePostSubmit}>
          <input
            type="text"
            name="title"
            placeholder="Title"
            value={postForm.title}
            onChange={(e) => handleInputChange(e, setPostForm)}
            required
          />
          <br /><br />
          <input
            type="text"
            name="description"
            placeholder="Description"
            value={postForm.description}
            onChange={(e) => handleInputChange(e, setPostForm)}
            required
          />
          <br /><br />
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={postForm.username}
            onChange={(e) => handleInputChange(e, setPostForm)}
            required
          />
          <br /><br />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={postForm.password}
            onChange={(e) => handleInputChange(e, setPostForm)}
            required
          />
          <br /><br />
          <button type="submit">Submit Post</button>
        </form>
      </div>

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
            className="modal-content"
            style={{
              backgroundColor: 'white',
              padding: '2rem',
              borderRadius: '8px',
              boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
              width: '300px',
              textAlign: 'center',
            }}
          >
            <h2>Confirm Like</h2>
            <form onSubmit={handleLike}>
              <input
                type="text"
                name="username"
                placeholder="Username"
                value={likeForm.username}
                onChange={(e) => handleInputChange(e, setLikeForm)}
                required
                style={{ width: '100%', marginBottom: '1rem', padding: '0.5rem' }}
              />
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={likeForm.password}
                onChange={(e) => handleInputChange(e, setLikeForm)}
                required
                style={{ width: '100%', marginBottom: '1rem', padding: '0.5rem' }}
              />
              <button type="submit" style={{ marginRight: '1rem', padding: '0.5rem 1rem' }}>Confirm</button>
              <button
                type="button"
                onClick={() => setLikeModal({ isOpen: false, postId: null })}
                style={{ padding: '0.5rem 1rem' }}
              >
                Cancel
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

function App() {
  return (
    <Router>
      <div>
        <nav style={{ padding: '1rem', backgroundColor: '#f0f0f0' }}>
          <Link to="/" style={{ marginRight: '1rem' }}>Home & Register</Link>
        </nav>
        <Routes>
          <Route path="/" element={<Home />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
