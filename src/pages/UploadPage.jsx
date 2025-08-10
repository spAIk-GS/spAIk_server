import "./UploadPage.scss";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { useState, useRef, useEffect } from "react";
import axios from "axios";


const UploadPage = () => {

  
  const [selectedFile, setSelectedFile] = useState(null);
  const fileInputRef = useRef();
  const [dragOver, setDragOver] = useState(false);
  const [step, setStep] = useState(1); //단계 상태(1: 업로드, 2: preview 화면)
  const [videoMetadata, setVideoMetadata] = useState({
    duration: null,
    width: null,
    height: null,
  });
  const [thumbnailFile, setThumbnailFile] = useState(null);
  const [videoUrl, setVideoUrl] = useState(null);

  // selectedFile이 바뀔 때마다 URL 생성하고 정리
  useEffect(() => {
    if (selectedFile) {
      const url = URL.createObjectURL(selectedFile);
      setVideoUrl(url);

      return () => {
        URL.revokeObjectURL(url); // 정리
      };
    }
  }, [selectedFile]);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file && isValidExtension(file.name)) {
      setSelectedFile(file);
    } else {
      alert("MP4 또는 MOV 파일만 업로드할 수 있습니다.");
      e.target.value = ""; // 파일 선택 초기화
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    setDragOver(false);
    const file = e.dataTransfer.files[0];
    if (file && isValidExtension(file.name)) {
      setSelectedFile(file);
    } else {
      alert("MP4 또는 MOV 파일만 업로드할 수 있습니다.");
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
    setDragOver(true);
  };

  const handleDragLeave = () => {
    setDragOver(false);
  };


  const handleButtonClick = () => {
    fileInputRef.current.click();
  };

  const isValidExtension = (fileName) => {
    const allowedExtensions = ['mp4', 'mov'];
    const ext = fileName.split('.').pop().toLowerCase();
    return allowedExtensions.includes(ext);
  };

  const handleArrowClick = () => {
    if (selectedFile) {
      setStep(2); // ✅ 두 번째 화면으로 전환
    } else {
      alert("먼저 파일을 선택해주세요.");
    }
  };

  const handleBack = () => {
    setSelectedFile(null);
    setStep(1);
  };

  const formatDuration = (duration) => {
    const minutes = Math.floor(duration / 60);
    const seconds = Math.floor(duration % 60).toString().padStart(2, '0');
    return `${minutes}:${seconds}`;
  };

  const generateThumbnailName = (originalFileName) => {
    const baseName = originalFileName.split(".").slice(0, -1).join(".");
    return `${baseName}_thumbnail.jpg`;
  };

  const captureThumbnail = (videoUrl) => {
    return new Promise((resolve, reject) => {
      const video = document.createElement("video");
      video.src = videoUrl;
      video.crossOrigin = "anonymous";
      video.currentTime = 1;

      video.onloadeddata = () => {
        video.currentTime = 1;
      };

      video.onseeked = () => {
        const canvas = document.createElement("canvas");
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        const ctx = canvas.getContext("2d");
        ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
        canvas.toBlob((blob) => {
          if (blob) {
            const thumbnailFileName = generateThumbnailName(selectedFile.name);
            const thumbFile = new File([blob], thumbnailFileName, {
              type: "image/jpeg",
            });
            resolve(thumbFile);
          } else {
            reject(new Error("썸네일 생성 실패"));
          }
        }, "image/jpeg", 0.95);
      };

      video.onerror = (e) => reject(new Error("비디오 로드 실패"));
    });
  };

  const getPresignedUrl = async (fileName, type) => {
    const endpoint =
      type === "video"
        ? "http://localhost:8080/videos/presign"
        : "http://localhost:8080/thumbnails/presign";
    const token = localStorage.getItem("accessToken");

    const response = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ fileName }),
    });

    if (!response.ok) throw new Error("Presigned URL 요청 실패");

    const data = await response.json();
    return data.url;
  };

  const uploadToS3 = async (file, url) => {
    const response = await fetch(url, {
      method: "PUT",
      headers: {
        "Content-Type": "video/mp4",
      },
      body: file,
    });

    if (!response.ok) {
      const errorText = await response.text();
    console.error("S3 응답 본문:", errorText); // 원인 메시지 확인 가능
    throw new Error("S3 업로드 실패");
    }
  }

  const requestAnalysis = async (videoFileName) => {
    const token = localStorage.getItem("accessToken");
    const user = JSON.parse(localStorage.getItem("user"));
    const userId = user?.id;

    const response = await fetch("/videos/submit", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        videoFileName,
        userId,
      }),
    });

    if (!response.ok) throw new Error("AI 분석 요청 실패");
    const result = await response.json();
    console.log("AI 분석 요청 성공:", result);
    alert("AI 분석이 시작되었습니다!");
  };

  const handleUpload = async () => {
    if (!selectedFile) return alert("먼저 파일을 선택해주세요.");

    try {
      // 썸네일 생성
      const thumbnail = await captureThumbnail(videoUrl);
      setThumbnailFile(thumbnail);

      // presigned URL 요청
      const [videoUrlRes, thumbUrlRes] = await Promise.all([
        getPresignedUrl(selectedFile.name, "video"),
        // getPresignedUrl(thumbnail.name, "thumbnail"),
      ]);

      // 업로드
      await Promise.all([
        uploadToS3(selectedFile, videoUrlRes),
        // uploadToS3(thumbnail, thumbUrlRes),
      ]);

      // 분석 요청
      await requestAnalysis(selectedFile.name);
    } catch (error) {
      console.error("Upload or Analysis Error:", error);
      alert("업로드 중 오류 발생: " + error.message);
    }
  };


  return (
    <div className="upload-page">
      <Header />

      <main className="upload-page__main">
        <div 
          className={`upload-box ${dragOver ? "drag-over" : ""}`}
          onDrop={handleDrop}
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
        >
          {step === 1 ? (
            <>
              <h2 className="upload-title">Upload your Presentation</h2>
              <p className="upload-description">
                MP4 또는 MOV 형식의 영상 파일을 끌어 놓습니다.
              </p>
              <div className="upload-button">
                <button className="file-btn" onClick={handleButtonClick}>
                  파일 선택
                </button>
                <span className="arrow" onClick={handleArrowClick}>»</span>
                <input
                  type="file"
                  accept=".mp4, .mov" //확장자 제한
                  ref={fileInputRef}
                  style={{ display: "none" }}
                  onChange={handleFileChange}
                />
              </div>

              {selectedFile && (
                <p className="file-name">선택된 파일: {selectedFile.name}</p>
              )}
            </>
          ) : (
            <>
              <div className="video-info-box">
                <div className="video-info-text">
                  <p><strong>File Name:&nbsp;</strong> {selectedFile?.name}</p>
                  <p><strong>File Size:&nbsp;</strong> {(selectedFile?.size / (1024 * 1024)).toFixed(2)} MB</p>
                  <p><strong>Video Length:&nbsp;</strong> 
                    {videoMetadata.duration !== null
                      ? formatDuration(videoMetadata.duration)
                      : "Loading..."}
                  </p>
                  <p><strong>Resolution:&nbsp;</strong> 
                    {videoMetadata.width && videoMetadata.height
                      ? `${videoMetadata.width} x ${videoMetadata.height}`
                      : "Loading..."}
                  </p>
                </div>

                <div className="video-preview">
                  <video
                    src={videoUrl}
                    controls
                    preload="metadata"
                    style={{
                      borderRadius: '12px',
                      maxWidth: '320px',
                      maxHeight: '180px',
                      objectFit: 'cover',
                    }}
                    onLoadedMetadata={(e) => {
                      const video = e.target;
                      setVideoMetadata({
                        duration: video.duration,
                        width: video.videoWidth,
                        height: video.videoHeight,
                      });
                    }}
                  />
                </div>
              </div>

              <div className="analyze-button-container">
                <button className="analyze-button" onClick={handleUpload}>
                  Start AI Analysis!
                </button>
              </div>
            </>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default UploadPage;