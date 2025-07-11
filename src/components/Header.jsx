import "./Header.scss";
import logoImage from '../assets/logo.png';

const Header = () => {
  return (
    <header className="header">
      <a href="/" className="logo">
        <img src={logoImage} alt="logo" className="logo__img" />
        <span className="logo__text">spAIk</span>
      </a>
      <nav className="nav">
        <a href="#">Upload</a>
        <a href="#">Product</a>
        <a href="#">Community</a>
        <a href="#">History</a>
        <a href="login">Login</a>
      </nav>
    </header>
  );
};

export default Header;
