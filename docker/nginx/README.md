# Nginx Configuration for E-Salle ENSAA

This directory contains the Nginx reverse proxy configuration for the E-Salle ENSAA application.

## 📁 Files

- `nginx.conf` - Main Nginx configuration file
- `ssl/` - SSL certificates directory (for HTTPS)

## 🔧 Configuration Overview

### Features

1. **HTTPS Support**
   - SSL/TLS termination
   - HTTP to HTTPS redirect
   - Security headers (HSTS, X-Frame-Options, etc.)

2. **Reverse Proxy**
   - Proxies requests to Tomcat (webapp:8080)
   - Load balancing ready (upstream configuration)

3. **Performance**
   - Gzip compression
   - Static file caching
   - Keep-alive connections

4. **Security**
   - Rate limiting (API and login endpoints)
   - Security headers
   - SSL/TLS configuration

5. **Logging**
   - Access logs
   - Error logs
   - Custom log format

## 🚀 Usage

### Development

1. **Generate SSL certificates:**
   ```bash
   # Windows
   .\generate-ssl.ps1
   
   # Linux/Mac
   ./generate-ssl.sh
   ```

2. **Start with Nginx:**
   ```bash
   docker-compose up -d
   ```

3. **Access application:**
   - HTTPS: `https://localhost`
   - HTTP: `http://localhost` (redirects to HTTPS)

### Production

1. **Replace SSL certificates:**
   - Place your production certificates in `ssl/` directory:
     - `nginx.crt` (or your certificate name)
     - `nginx.key` (or your private key name)
   - Update `nginx.conf` with correct certificate paths

2. **Update server name:**
   - Change `server_name localhost;` to your domain name

3. **Review security settings:**
   - Adjust rate limiting if needed
   - Review SSL protocols and ciphers
   - Update security headers

## 📝 Configuration Details

### SSL/TLS

- **Protocols:** TLSv1.2, TLSv1.3
- **Ciphers:** Modern, secure ciphers
- **Session cache:** 10 minutes
- **HSTS:** Enabled (1 year)

### Rate Limiting

- **API endpoints:** 10 requests/second (burst: 20)
- **Login endpoint:** 1 request/second (burst: 3)
- **Zones:** 10MB memory per zone

### Upstream

- **Backend:** webapp:8080
- **Keep-alive:** 32 connections

### Static Files

- **Cache:** 1 year
- **Types:** CSS, JS, images, SVG

## 🔒 Security Headers

- `Strict-Transport-Security`: HSTS (1 year)
- `X-Frame-Options`: DENY
- `X-Content-Type-Options`: nosniff
- `X-XSS-Protection`: 1; mode=block
- `Referrer-Policy`: strict-origin-when-cross-origin

## 🐛 Troubleshooting

### Issue: SSL certificate errors

**Solution:**
1. Verify certificates exist: `ls docker/nginx/ssl/`
2. Check certificate permissions
3. Regenerate if needed: `.\generate-ssl.ps1`

### Issue: 502 Bad Gateway

**Solution:**
1. Check if webapp container is running: `docker-compose ps`
2. Verify webapp is accessible: `curl http://localhost:8080/health`
3. Check Nginx logs: `docker-compose logs nginx`

### Issue: Rate limiting too strict

**Solution:**
1. Edit `nginx.conf`
2. Adjust `limit_req_zone` rates
3. Restart Nginx: `docker-compose restart nginx`

## 📚 Additional Resources

- [Nginx Documentation](https://nginx.org/en/docs/)
- [SSL Configuration Best Practices](https://ssl-config.mozilla.org/)
- [Rate Limiting Guide](https://www.nginx.com/blog/rate-limiting-nginx/)

## ⚠️ Important Notes

1. **Development vs Production:**
   - Development uses self-signed certificates
   - Production should use Let's Encrypt or trusted CA certificates

2. **Security:**
   - Never commit private keys (`.key` files) to version control
   - Rotate certificates regularly
   - Keep Nginx updated

3. **Performance:**
   - Adjust worker processes based on CPU cores
   - Monitor connection limits
   - Review cache settings

---

**Last Updated:** 7 Novembre 2025

