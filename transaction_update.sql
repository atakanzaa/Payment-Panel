-- Remove payment_method_id column from transactions table
ALTER TABLE transactions DROP CONSTRAINT IF EXISTS transactions_payment_method_id_fkey;
ALTER TABLE transactions ALTER COLUMN payment_method_id DROP NOT NULL;

