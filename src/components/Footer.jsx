import "./Footer.scss";

const Footer = () => {
  return (
    <footer className="footer">
      <div className="left">
        <div className="logo">spAIk</div>
        <p>spAIk’s mission is to enhance blabla bla blablabla bla bla.</p>
      </div>
      <div className="right">
        <div className="section">
          <strong>About</strong>
          <div className="icons">
            <span>🐙</span>
            <span>📝</span>
            <span>💬</span>
          </div>
        </div>
        <div className="section">
          <strong>Support</strong>
          <div className="icons">
            <span>🎓</span>
            <span>🚀</span>
            <span>🗣️</span>
          </div>
        </div>
      </div>
      <div className="copyright">@2025 spAIk, Team.</div>
    </footer>
  );
};

export default Footer;
