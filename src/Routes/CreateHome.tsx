import React, { useState, ChangeEvent, FormEvent } from "react";
import { MyDiv } from "../Styles/CreateHomestyle";
import styles from "../Styles/Main.module.css";
import { useNavigate } from "react-router-dom";

interface WorldcupDto {
  name: string;
  description: string;
  category: string;
}

function CreateHome() {
  const navigate = useNavigate();
  const [name, setName] = useState<string>(""); // 월드컵 이름
  const [description, setDescription] = useState<string>(""); // 월드컵 설명
  const [category, setCategory] = useState<string>(""); // 카테고리
  const [image, setImage] = useState<File | null>(null); // 업로드할 이미지 파일
  const [imageUrl, setImageUrl] = useState<string | null>(null); // 이미지 미리보기 URL

  // 이미지 선택 핸들러
  const handleImageChange = (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]; // 선택된 파일
    if (file) {
      setImage(file); // 이미지 파일 저장

      // 이미지 미리보기 설정
      const reader = new FileReader();
      reader.onloadend = () => {
        setImageUrl(reader.result as string); // 이미지 미리보기 URL 설정
      };
      reader.readAsDataURL(file); // 파일을 DataURL로 읽음
    }
  };

  // 폼 제출 핸들러
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    // FormData 객체 생성
    const formData = new FormData();
    const worldcupDto: WorldcupDto = {
      name,
      description,
      category,
    };

    // JSON 데이터를 문자열로 변환하여 FormData에 추가
    formData.append(
      "data",
      new Blob([JSON.stringify(worldcupDto)], { type: "application/json" })
    );
    if (image) {
      formData.append("image", image); // 이미지 파일을 'image'라는 키로 추가
    }

    // 서버로 전송
    try {
      const token = localStorage.getItem("jwtToken");
      const response = await fetch(`http://localhost:8080/worldcup`, {
        method: "POST",
        headers: {
          Authorization: `${token ? token : ""}`, // Content-Type은 설정하지 않음
        },
        body: formData,
      });

      if (response.ok) {
        const result = await response.json();
        console.log("Worldcup created successfully:", result);

        // 서버에서 응답받은 이미지 URL 설정
        setImageUrl(`http://localhost:8080${result.imageUrl}`);
        // 성공 시 처리
        alert("월드컵 생성이 완료되었습니다.");
        navigate("/");
      } else {
        console.log(response);
        console.log(token);
        alert("월드컵 생성에 실패하였습니다.");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("월드컵 생성에 실패하였습니다.");
    }
  };

  return (
    <MyDiv>
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <input
            type="text"
            placeholder="월드컵 이름을 입력해주세요."
            value={name}
            onChange={(e) => setName(e.target.value)}
            className={styles.input_field}
          />
        </div>
        <div className={styles.field}>
          <input
            type="text"
            value={description}
            placeholder="상세 설명을 입력해주세요."
            onChange={(e) => setDescription(e.target.value)}
            className={styles.input_field}
          />
        </div>
        <div className={styles.field}>
          <input
            type="text"
            placeholder="카테고리를 입력해주세요"
            value={category}
            onChange={(e) => setCategory(e.target.value)}
            className={styles.input_field}
          />
        </div>

        {imageUrl ? (
          <div>
            <img
              src={imageUrl}
              alt="미리보기"
              style={{ width: "200px", height: "175px" }}
            />
            <input
              id="file"
              type="file"
              accept="image/*"
              onChange={handleImageChange}
            />
          </div>
        ) : (
          <form className={styles.file_upload_form}>
            <label className={styles.file_upload_label}>
              <div className={styles.file_upload_design}>
                <svg viewBox="0 0 640 512" height="1em">
                  <path d="M144 480C64.5 480 0 415.5 0 336c0-62.8 40.2-116.2 96.2-135.9c-.1-2.7-.2-5.4-.2-8.1c0-88.4 71.6-160 160-160c59.3 0 111 32.2 138.7 80.2C409.9 102 428.3 96 448 96c53 0 96 43 96 96c0 12.2-2.3 23.8-6.4 34.6C596 238.4 640 290.1 640 352c0 70.7-57.3 128-128 128H144zm79-217c-9.4 9.4-9.4 24.6 0 33.9s24.6 9.4 33.9 0l39-39V392c0 13.3 10.7 24 24 24s24-10.7 24-24V257.9l39 39c9.4 9.4 24.6 9.4 33.9 0s9.4-24.6 0-33.9l-80-80c-9.4-9.4-24.6-9.4-33.9 0l-80 80z"></path>
                </svg>
              </div>
              <input
                id="file"
                type="file"
                accept="image/*"
                onChange={handleImageChange}
              />
            </label>
          </form>
        )}
        <button type="submit" className={styles.button1}>
          월드컵 생성
        </button>
      </form>
    </MyDiv>
  );
}

export default CreateHome;
