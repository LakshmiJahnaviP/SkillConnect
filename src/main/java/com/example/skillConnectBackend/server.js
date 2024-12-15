const express = require('express');
const mongoose = require('mongoose');
const userRoutes = require('./routes/userRoutes');  // Import user-related routes

const app = express();
const PORT = process.env.PORT || 8080;

// Middleware
app.use(express.json());

// Use user-related routes
app.use('/api', userRoutes);  // Prefix routes with /api

// Connect to MongoDB (or your DB)
mongoose.connect('mongodb://localhost:27017/myDatabase', { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log('Connected to MongoDB'))
  .catch(err => console.log('Failed to connect to MongoDB:', err));

// Start the server
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
