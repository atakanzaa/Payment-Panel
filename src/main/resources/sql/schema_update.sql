-- Add payment confirmation fields to transactions table
ALTER TABLE IF EXISTS transactions
  ADD COLUMN IF NOT EXISTS payment_confirmed BOOLEAN NOT NULL DEFAULT FALSE,
  ADD COLUMN IF NOT EXISTS payment_date TIMESTAMP;

-- Update existing records to set payment_confirmed to false
UPDATE transactions SET payment_confirmed = false WHERE payment_confirmed IS NULL; 