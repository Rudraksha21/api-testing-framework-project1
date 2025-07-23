
---

## ✅ Test Summary

| Test Name                         | Status | Description |
|----------------------------------|--------|-------------|
| `testCreateNewItem`              | ✅ Pass | Creates a new item with valid name and description |
| `testCreateItemWithMissingFields`| ✅ Pass | Tries to create an item with missing fields (expects 400) |
| `testGetAllItems`                | ✅ Pass | Retrieves the list of all available items |
| `testGetSpecificItem`           | ✅ Pass | Fetches a specific item by ID |
| `testGetNonExistentItem`        | ✅ Pass | Tries to fetch an item that does not exist (expects 404) |
| `testUpdateItem`                | ✅ Pass | Updates an existing item’s name and description |
| `testUpdateNonExistentItem`     | ✅ Pass | Attempts to update a non-existent item (expects 404) |
| `testDeleteItem`                | ✅ Pass | Deletes an existing item and confirms deletion |
| `testDeleteNonExistentItem`     | ✅ Pass | Attempts to delete a non-existent item (expects 404) |

---

## 🧪 Sample API Responses

### ➕ `POST /api/items`
**Status:** `201 Created`  
**Response:**
```json
{
  "id": 3,
  "name": "New Test Item",
  "description": "This is a description for a new item created by API test."
}
