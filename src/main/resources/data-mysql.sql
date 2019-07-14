DELETE FROM bookings;
ALTER TABLE bookings AUTO_INCREMENT = 1;

INSERT INTO bookings (adults_count, check_in_date, check_out_date, children_count, person_name, room_type)
VALUES (2, '2019-06-22', '2019-06-25', 3, 'Mykola', 'STANDART'),
       (2, '2019-06-15', '2019-06-26', 0, 'Halyna', 'STANDART'),
       (1, '2019-06-01', '2019-06-30', 0, 'Maksym M.', 'SUITE');