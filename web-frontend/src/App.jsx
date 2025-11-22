import { useState, useEffect } from 'react'

const API_URL = '/api'

function App() {
  const [todos, setTodos] = useState([])
  const [newTodo, setNewTodo] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  // Fetch todos on mount
  useEffect(() => {
    fetchTodos()
  }, [])

  const fetchTodos = async () => {
    try {
      setLoading(true)
      setError(null)
      const response = await fetch(`${API_URL}/todos`)
      if (!response.ok) throw new Error('Failed to fetch todos')
      const data = await response.json()
      setTodos(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const addTodo = async (e) => {
    e.preventDefault()
    if (!newTodo.trim()) return

    try {
      setError(null)
      const response = await fetch(`${API_URL}/todos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: newTodo, completed: false })
      })
      if (!response.ok) throw new Error('Failed to add todo')
      const data = await response.json()
      setTodos([...todos, data])
      setNewTodo('')
    } catch (err) {
      setError(err.message)
    }
  }

  const toggleTodo = async (id, completed) => {
    try {
      setError(null)
      const todo = todos.find(t => t.id === id)
      const response = await fetch(`${API_URL}/todos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ...todo, completed: !completed })
      })
      if (!response.ok) throw new Error('Failed to update todo')
      const data = await response.json()
      setTodos(todos.map(t => t.id === id ? data : t))
    } catch (err) {
      setError(err.message)
    }
  }

  const deleteTodo = async (id) => {
    try {
      setError(null)
      const response = await fetch(`${API_URL}/todos/${id}`, {
        method: 'DELETE'
      })
      if (!response.ok) throw new Error('Failed to delete todo')
      setTodos(todos.filter(t => t.id !== id))
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="container">
      <h1>TODO List</h1>

      {error && <div className="error">{error}</div>}

      <form className="input-container" onSubmit={addTodo}>
        <input
          type="text"
          value={newTodo}
          onChange={(e) => setNewTodo(e.target.value)}
          placeholder="Add a new task..."
        />
        <button type="submit" disabled={!newTodo.trim()}>Add</button>
      </form>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : todos.length === 0 ? (
        <div className="empty">No todos yet. Add one above!</div>
      ) : (
        <ul className="todo-list">
          {todos.map(todo => (
            <li key={todo.id} className="todo-item">
              <input
                type="checkbox"
                checked={todo.completed}
                onChange={() => toggleTodo(todo.id, todo.completed)}
              />
              <span className={todo.completed ? 'completed' : ''}>
                {todo.title}
              </span>
              <button className="delete-btn" onClick={() => deleteTodo(todo.id)}>
                Delete
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

export default App
