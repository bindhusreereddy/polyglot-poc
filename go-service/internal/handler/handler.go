package handler

import (
    "github.com/gofiber/fiber/v2"
    "github.com/your-org/go-transaction-service/internal/model"
    "github.com/your-org/go-transaction-service/internal/service"
)

func CreateTransaction(c *fiber.Ctx) error {
    var tx model.Transaction
    if err := c.BodyParser(&tx); err != nil {
        return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{"error": "invalid input"})
    }
    created, err := service.Create(tx)
    if err != nil {
        return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map{"error": err.Error()})
    }
    return c.Status(fiber.StatusCreated).JSON(created)
}

func GetTransaction(c *fiber.Ctx) error {
    id := c.Params("id")
    tx, err := service.GetByID(id)
    if err != nil {
        return c.Status(fiber.StatusNotFound).JSON(fiber.Map{"error": "transaction not found"})
    }
    return c.JSON(tx)
}
