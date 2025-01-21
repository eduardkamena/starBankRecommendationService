-- slq query with data for tests

-- Создаем тестового пользователя
INSERT INTO users (id, username, first_name, last_name) VALUES
        ('cd515076-5d8a-44be-930e-8d4fcb79f42d',
        'testUser',
        'Test',
        'User'
        );

-- Создаем тестовые продукты
INSERT INTO products (id, type) VALUES
        ('550e8400-e29b-41d4-a716-446655440000',
         'INVEST'
        ),
        ('550e8400-e29b-41d4-a716-446655440001',
         'DEBIT'
        ),
        ('550e8400-e29b-41d4-a716-446655440002',
         'CREDIT'
        );

-- Создаем тестовые транзакции для методов isUserOf, isActiveUserOf, isTransactionSumCompare и isTransactionSumCompareDepositWithdraw
INSERT INTO transactions (id, user_id, product_id, type, amount) VALUES
        ('550e8400-e29b-41d4-a716-446655440003',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440000',
         'DEPOSIT',
         100
        ), -- Транзакция с продуктом INVEST
        ('550e8400-e29b-41d4-a716-446655440004',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440000',
         'DEPOSIT',
         200
        ), -- Транзакция с продуктом INVEST
        ('550e8400-e29b-41d4-a716-446655440005',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440000',
         'DEPOSIT',
         300
        ), -- Транзакция с продуктом INVEST
        ('550e8400-e29b-41d4-a716-446655440006',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440000',
         'DEPOSIT',
         400
        ), -- Транзакция с продуктом INVEST
        ('550e8400-e29b-41d4-a716-446655440007',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440000',
         'DEPOSIT',
         500
        ), -- Транзакция с продуктом INVEST (5 транзакций для isActiveUserOf)
        ('550e8400-e29b-41d4-a716-446655440008',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440001',
         'DEPOSIT',
         100
        ), -- Транзакция с продуктом DEBIT (для отрицательного сценария isActiveUserOf)
        ('550e8400-e29b-41d4-a716-446655440009',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440000',
         'DEPOSIT',
         1500
        ), -- Транзакция с суммой 1500 для isTransactionSumCompare
        ('550e8400-e29b-41d4-a716-446655440010',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440000',
         'DEPOSIT',
         2000
        ), -- Депозит 2000 для метода isTransactionSumCompareDepositWithdraw
        ('550e8400-e29b-41d4-a716-446655440011',
         'cd515076-5d8a-44be-930e-8d4fcb79f42d',
         '550e8400-e29b-41d4-a716-446655440000',
         'WITHDRAW',
         1000
        ); -- Снятие 1000 для метода isTransactionSumCompareDepositWithdraw

-- Создаем тестовую рекомендацию
INSERT INTO recommendations (id, product_name, product_id, product_text) VALUES
        ('550e8400-e29b-41d4-a716-446655440012',
         'Invest 500',
         '550e8400-e29b-41d4-a716-446655440000',
         'Описание Invest 500'
        );

-- Создаем тестовое правило для рекомендации
INSERT INTO rules (id, query, negate, recommendations_id) VALUES
        ('550e8400-e29b-41d4-a716-446655440013',
         'USER_OF',
         false,
         '550e8400-e29b-41d4-a716-446655440012'
        );

-- Создаем тестовую статистику для рекомендации
INSERT INTO stats (id, count, recommendations_id) VALUES
        ('550e8400-e29b-41d4-a716-446655440014',
         0,
         '550e8400-e29b-41d4-a716-446655440012'
        );
