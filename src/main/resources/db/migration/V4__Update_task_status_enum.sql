-- Update default status values to match TaskStatus enum
-- Change any existing PENDING status to NEW to match the enum
UPDATE tasks SET status = 'NEW' WHERE status = 'PENDING';

-- Update the default status for new rows
ALTER TABLE tasks ALTER COLUMN status SET DEFAULT 'NEW';

-- Remove priority column since it's not used in the TaskStatus enum
ALTER TABLE tasks DROP COLUMN IF EXISTS priority;

-- Remove unused indexes
DROP INDEX IF EXISTS idx_task_user_priority;