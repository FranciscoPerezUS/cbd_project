import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import './App.css';

function Home() {
  const [posts, setPosts] = useState([]);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  useEffect(() => {
    const fetchPosts = async () => {
      const response = await fetch('http://localhost:8080/posts');
      if (response.ok) {
        const data = await response.json();
        setPosts(data);
      } else {
        alert('Failed to fetch posts');
      }
    };

    fetchPosts();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch('http://localhost:8080/usuarios', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, username, password })
    });

    if (response.ok) {
      alert('User registered successfully!');
      setName('');
      setEmail('');
    } else {
      alert('Failed to register user');
    }
  };

  return (
    <div style={{ display: 'flex', padding: '2rem' }}>
      {/* Sidebar for posts */}
      <div style={{ width: '30%', marginRight: '2rem' }}>
        <h2>Posts</h2>
        <ul>
          {posts.map((post, index) => (
            <li key={index}>
              <h4>{post.title}</h4>
              <p>{post.description}</p>
              <p>Name: {post.name} Email: {post.email}</p>
            </li>
          ))}
        </ul>
      </div>
      {/* Registration form */}
      <div style={{ width: '70%' }}>
        <h1>Register</h1>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
          <br /><br />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <br /><br />
          <input
            type="username"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <br /><br />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <br /><br />
          <button type="submit">Register</button>
        </form>
      </div>
    </div>
  );
}

function App() {
  return (
    <Router>
      <div>
        {/* Navbar */}
        <nav style={{ padding: '1rem', backgroundColor: '#f0f0f0' }}>
          <Link to="/" style={{ marginRight: '1rem' }}>Home & Register</Link>
        </nav>

        {/* Routes */}
        <Routes>
          <Route path="/" element={<Home />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
