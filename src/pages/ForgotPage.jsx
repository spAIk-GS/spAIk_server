import React, { useState } from "react";
import Header from "../components/Header";
import "./ForgotPage.scss";

const ForgotPage = () => {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!email) {
      setError("Please Enter your Email.");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/auth/forgot-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });

      if (response.ok) {
        setMessage("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        setError("");
      } else {
        const data = await response.json();
        setError(data.message || "오류가 발생했습니다.");
        setMessage("");
      }
    } catch (err) {
      console.error(err);
      setError("서버와 통신 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="forgot-page">
    <Header />
      <div className="forgot-container">
        <h2>Find your Password</h2>
        <form onSubmit={handleSubmit}>
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            placeholder="Enter your email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button type="submit">Send Password Reset Link</button>
        </form>
        {message && <p className="success-message">{message}</p>}
        {error && <p className="error-message">{error}</p>}
      </div>
    </div>
  );
};

export default ForgotPage;