import React, { useState, useContext } from 'react';
import AppContext from '../context/AppContext';
import { useNavigate } from 'react-router-dom';
import './LoginPage.scss';
import googleLogo from '../assets/google_logo.png';
import Header from "../components/Header";


function LoginPage() {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const { setIsLoggedIn } = useContext(AppContext);

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    if (!email.trim() || !password.trim()) {
      setErrorMsg('이메일과 비밀번호를 모두 입력해주세요.');
      return;
    }


    //더미 로그인 처리
    //스프링부트 연동 후에 제거
    if (email === 'test@example.com' && password === '123456') {
      setIsLoggedIn(true);
      sessionStorage.setItem("isLoggedIn", "true"); // 저장
      setErrorMsg('');
      navigate('/');
    } else {
      setErrorMsg('Invalid Email or Password');
    }

    //나중에 스프링부트 연결 시 아래 코드 주석 해제
    try {
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const data = await response.json();
        setIsLoggedIn(true); // 로그인 성공 처리

        // Bearer 형식 저장
        localStorage.setItem('token', `Bearer ${data.token}`);
        
        // 유저 정보 저장 (선택)
        if (data.user?.email) {
          localStorage.setItem('userEmail', data.user.email);
        }

        navigate('/');
      } else {
        let errMsg = '로그인에 실패했습니다.';
        try {
          const err = await response.json();
          errMsg = err.error || err.message || errMsg;
        } catch (e) {
          console.warn('에러 응답 파싱 실패', e);
        }
        setErrorMsg(errMsg);
      }
    } catch (error) {
      console.error('Error: ', error);
      setErrorMsg('서버 오류가 발생했습니다.');
    }

  };

 return (
  <div className="login-page"> 
    <div className="login-page-wrapper">
      <Header />
      <div className="login-content">
        <div className="login-card">
          <h2>Login For your Account</h2>

          <label htmlFor="email">Email</label>
          <input 
            type="email" 
            id="email" 
            placeholder="Enter your email"
            value={email}
            onChange={(e) => setEmail(e.target.value)} 
          /> 

          <div className="password-label-row">
            <label htmlFor="password">Password</label>
            <a href="#" className="forgot-link">Forgot?</a>
          </div>

          <div className="password-wrapper">
            <input 
              type="password" 
              id="password" 
              placeholder="Enter your password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          {errorMsg && <p className="error-msg">{errorMsg}</p>}

        {/* <button className="login-btn">Login</button>   e-branch2 */}
        
          <button type="submit" className="login-btn" onClick={handleLogin}>Login</button>
          <div className="or-divider">or</div>
          <button className="google-btn">
            <span>Sign In with Google</span>
            <img src={googleLogo} alt="Google logo" />  
          </button>

          <p className="signup-text">
            No Account? <a href="signup">Sign Up</a>
          </p>
        </div>

        <p className="terms-text">
          By using spAlk, you agree to our <a href="#">Terms</a> and <a href="#">Privacy Policy</a>
        </p>
      </div>
    </div>
  </div>
  );
}

export default LoginPage;
