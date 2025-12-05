// 전체 회원 조회 스크립트
import * as mysql from "mysql2/promise";

async function checkMembers() {
    console.log("🔍 전체 회원 조회");
    console.log("=".repeat(80));

    const configs = [
        {
            host: "localhost",
            user: "bang_user",
            password: "bang_password",
            database: "board_test",
        },
    ];

    let connection: mysql.Connection | null = null;
    for (const config of configs) {
        try {
            connection = await mysql.createConnection(config);
            console.log(`✅ DB 연결 성공 (user: ${config.user})`);

            // 전체 회원 조회
            const [rows] = await connection.query(
                "SELECT member_id, ID, password, nickname, email, signup_date FROM member LIMIT 20"
            );

            console.log(`\n📋 총 ${(rows as any[]).length}명의 회원이 있습니다:\n`);
            
            (rows as any[]).forEach((member, index) => {
                console.log(`${index + 1}. member_id: ${member.member_id}`);
                console.log(`   ID: ${member.ID}`);
                console.log(`   password: ${member.password}`);
                console.log(`   nickname: ${member.nickname}`);
                console.log(`   email: ${member.email}`);
                console.log(`   signup_date: ${member.signup_date}`);
                console.log("");
            });

            // 아이디 "1"인 회원 찾기
            const [member1] = await connection.query(
                "SELECT member_id, ID, password, nickname, email FROM member WHERE ID = ?",
                ["1"]
            );

            if ((member1 as any[]).length > 0) {
                console.log("\n✅ 아이디 '1'인 회원을 찾았습니다:");
                const m = (member1 as any[])[0];
                console.log(`   member_id: ${m.member_id}`);
                console.log(`   ID: ${m.ID}`);
                console.log(`   password: ${m.password}`);
                console.log(`   nickname: ${m.nickname}`);
            } else {
                console.log("\n❌ 아이디 '1'인 회원을 찾을 수 없습니다.");
            }

            await connection.end();
            return;
        } catch (error: any) {
            if (connection) {
                await connection.end().catch(() => {});
            }
            console.log(`❌ ${config.user} 연결 실패: ${error.message}`);
        }
    }

    console.log("\n❌ DB 연결 실패");
}

checkMembers().catch(console.error);

