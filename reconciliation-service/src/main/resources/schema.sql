CREATE TABLE IF NOT EXISTS ledger_entry (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    transaction_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    amount DECIMAL NOT NULL,
    currency TEXT NOT NULL,
    status TEXT NOT NULL,
    sender_account TEXT NOT NULL,
    receiver_account TEXT NOT NULL,
    timestamp DATETIME NOT NULL,
    settlement_date DATE NOT NULL,
    settlement_report_id TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS settlement_report (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    report_id TEXT NOT NULL,
    total_amount DECIMAL NOT NULL,
    currency TEXT NOT NULL,
    status TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    merchant_id TEXT NOT NULL
);