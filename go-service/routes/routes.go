package routes

import (
    "github.com/gofiber/fiber/v2"
    "github.com/your-org/go-transaction-service/internal/handler"
)

func Setup(app *fiber.App) {
    api := app.Group("/api/v1")
    tx := api.Group("/transactions")
    tx.Post("/", handler.CreateTransaction)
    tx.Get("/:id", handler.GetTransaction)
}
