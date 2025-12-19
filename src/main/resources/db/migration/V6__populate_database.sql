INSERT INTO categories (name)
VALUES ('Fruits'),
       ('Vegetables'),
       ('Dairy'),
       ('Bakery'),
       ('Meat'),
       ('Beverages'),
       ('Snacks');
INSERT INTO products (name, price, description, category_id)
VALUES
-- Fruits
('Bananas (1 kg)', 1.49, 'Fresh ripe bananas, rich in potassium and perfect for daily consumption.', 1),
('Red Apples (1 kg)', 2.29, 'Crisp and sweet red apples, ideal for snacks and desserts.', 1),

-- Vegetables
('Tomatoes (1 kg)', 1.99, 'Fresh vine tomatoes suitable for salads and cooking.', 2),
('Potatoes (2 kg)', 2.49, 'All-purpose potatoes, ideal for frying, boiling, or baking.', 2),

-- Dairy
('Whole Milk (1 L)', 1.19, 'Pasteurized whole cow milk with 3.5% fat content.', 3),
('Cheddar Cheese (200 g)', 3.99, 'Aged cheddar cheese with a rich and sharp flavor.', 3),

-- Bakery
('White Bread Loaf', 1.49, 'Soft white bread baked fresh daily.', 4),

-- Meat
('Chicken Breast (1 kg)', 6.99, 'Boneless skinless chicken breast, high in protein.', 5),

-- Beverages
('Orange Juice (1 L)', 2.59, '100% pure orange juice with no added sugar.', 6),

-- Snacks
('Salted Potato Chips (150 g)', 1.99, 'Crispy salted potato chips, perfect for snacking.', 7);
