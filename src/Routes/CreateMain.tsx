import { ChangeEvent, FormEvent, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { MyDiv } from "../Styles/CreateHomestyle";
import styles from "../Styles/Main.module.css";

interface WorldcupDto {
  name: string;
}

interface MemberDto {
  name: string;
  image: File | null;
}

function CreateMain() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>(); // URL에서 월드컵 ID 가져오기
  const [members, setMembers] = useState<MemberDto[]>([]); // 멤버 목록
  const [imageUrl, setImageUrl] = useState<string | null>(null); // 이미지 미리보기 URL

  // 멤버 추가 핸들러
  const addMember = () => {
    setMembers([...members, { name: "", image: null }]);
  };

  // 멤버 이름 변경 핸들러
  const handleNameChange = (index: number, value: string) => {
    const updatedMembers = [...members];
    updatedMembers[index].name = value;
    setMembers(updatedMembers);
  };

  // 이미지 선택 핸들러
  const handleImageChange = (
    index: number,
    e: ChangeEvent<HTMLInputElement>
  ) => {
    const file = e.target.files?.[0]; // 선택된 파일
    if (file) {
      const updatedMembers = [...members];
      updatedMembers[index].image = file; // 이미지 파일 저장

      // 이미지 미리보기 설정
      const reader = new FileReader();
      reader.onloadend = () => {
        setImageUrl(reader.result as string); // 이미지 미리보기 URL 설정
      };
      reader.readAsDataURL(file); // 파일을 DataURL로 읽음

      setMembers(updatedMembers);
    }
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    // 각 멤버에 대해 서버로 전송
    for (const member of members) {
      const formData = new FormData();
      const memberDto: WorldcupDto = {
        name: member.name,
      };

      // JSON 데이터를 문자열로 변환하여 FormData에 추가
      formData.append(
        "data",
        new Blob([JSON.stringify(memberDto)], { type: "application/json" })
      );
      if (member.image) {
        formData.append("image", member.image); // 이미지 파일을 'image'라는 키로 추가
      }

      // 서버로 전송
      try {
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(
          `http://localhost:8080/member/${id}`, // URL에서 가져온 ID 사용
          {
            method: "POST",
            headers: {
              Authorization: `${token ? token : ""}`, // Content-Type은 설정하지 않음
            },
            body: formData,
          }
        );

        if (response.ok) {
          const result = await response.json();
          // alert("멤버 추가가 완료되었습니다.");
          navigate("/");
          console.log("Member added successfully:", result);
        } else {
          console.log(response);
        }
      } catch (error) {
        console.error("Error:", error);
      }
    }
  };

  return (
    <MyDiv>
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.memberContainer}>
          {members.map((member, index) => (
            <div key={index} className={styles.field}>
              <div className={styles.filed}>
                <input
                  className={styles.input_field}
                  type="text"
                  value={member.name}
                  placeholder="멤버 이름"
                  onChange={(e) => handleNameChange(index, e.target.value)}
                />
              </div>

              <div className={styles.field}>
                <input
                  className={styles.input_field}
                  type="file"
                  accept="image/*"
                  onChange={(e) => handleImageChange(index, e)}
                />
                {/* {imageUrl && <img src={imageUrl} alt="preview" width="200" />} */}
              </div>
            </div>
          ))}
        </div>
        <button type="button" onClick={addMember} className={styles.button1}>
          폼 추가
        </button>
        <button type="submit" className={styles.button1}>
          멤버 추가
        </button>
      </form>
    </MyDiv>
  );
}

export default CreateMain;
