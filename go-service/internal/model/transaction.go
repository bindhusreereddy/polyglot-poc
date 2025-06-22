package model

type Transaction struct {
    ID       string  `gorm:"primaryKey" json:"id"`
    UserID   string  `json:"user_id"`
    Amount   float64 `json:"amount"`
    Currency string  `json:"currency"`
    Status   string  `json:"status"`
}
