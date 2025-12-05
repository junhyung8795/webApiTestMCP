-- setup_permissions.sql
-- MCP가 디비에 접근하기 위해 필요한 권한 설정 스크립트

-- 1. 현재 사용자 확인
SELECT USER(), DATABASE();

-- 2. 기존 bang_user 사용자 확인
SELECT user, host FROM mysql.user WHERE user = 'bang_user';

-- 3. bang_user가 없으면 생성 (비밀번호: bang_password)
-- 이미 있으면 이 명령은 에러가 나지만 무시하고 다음으로 진행
CREATE USER IF NOT EXISTS 'bang_user'@'localhost' IDENTIFIED BY 'bang_password';

-- 4. board_test 데이터베이스에 대한 모든 권한 부여
GRANT ALL PRIVILEGES ON board_test.* TO 'bang_user'@'localhost';

-- 5. 권한 즉시 적용
FLUSH PRIVILEGES;

-- 6. 부여된 권한 확인
SHOW GRANTS FOR 'bang_user'@'localhost';

-- 7. board_test 데이터베이스 확인
SHOW DATABASES LIKE 'board_test';

-- 8. board_test의 테이블 확인
USE board_test;
SHOW TABLES;

-- 완료 메시지
SELECT '✅ 권한 설정 완료!' as status;


