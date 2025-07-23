const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.json());

let items = [
    { id: 1, name: 'Item A', description: 'Description for Item A' },
    { id: 2, name: 'Item B', description: 'Description for Item B' },
];

const generateId = () => {
    const maxId = items.length > 0 ? Math.max(...items.map(item => item.id)) : 0;
    return maxId + 1;
};

app.get('/api/items', (req, res) => {
    console.log('GET /api/items - All items requested');
    res.status(200).json(items);
});

app.get('/api/items/:id', (req, res) => {
    const id = parseInt(req.params.id);
    console.log(`GET /api/items/${id} - Specific item requested`);
    const item = items.find(i => i.id === id);

    if (item) {
        res.status(200).json(item);
    } else {
        res.status(404).json({ message: 'Item not found' });
    }
});

app.post('/api/items', (req, res) => {
    const { name, description } = req.body;
    console.log('POST /api/items - New item creation requested', req.body);

    if (!name || !description) {
        return res.status(400).json({ message: 'Name and description are required' });
    }

    const newItem = {
        id: generateId(),
        name,
        description
    };

    items.push(newItem);
    res.status(201).json(newItem);
});

app.put('/api/items/:id', (req, res) => {
    const id = parseInt(req.params.id);
    const { name, description } = req.body;
    console.log(`PUT /api/items/${id} - Item update requested`, req.body);

    const itemIndex = items.findIndex(i => i.id === id);

    if (itemIndex !== -1) {
        items[itemIndex] = {
            ...items[itemIndex],
            name: name || items[itemIndex].name,
            description: description || items[itemIndex].description
        };
        res.status(200).json(items[itemIndex]);
    } else {
        res.status(404).json({ message: 'Item not found' });
    }
});

app.delete('/api/items/:id', (req, res) => {
    const id = parseInt(req.params.id);
    console.log(`DELETE /api/items/${id} - Item deletion requested`);

    const initialLength = items.length;
    items = items.filter(i => i.id !== id);

    if (items.length < initialLength) {
        res.status(204).send();
    } else {
        res.status(404).json({ message: 'Item not found' });
    }
});

app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
    console.log('Available Endpoints:');
    console.log(`  GET    http://localhost:${PORT}/api/items`);
    console.log(`  GET    http://localhost:${PORT}/api/items/:id`);
    console.log(`  POST   http://localhost:${PORT}/api/items`);
    console.log(`  PUT    http://localhost:${PORT}/api/items/:id`);
    console.log(`  DELETE http://localhost:${PORT}/api/items/:id`);
});

