# GitHub এ Project Push করার নির্দেশনা

## ধাপ ১: Git Configuration (প্রথমবারের জন্য)

যদি Git configure করা না থাকে, তাহলে এই commands চালান:

```bash
git config --global user.name "CodingManiac44"
git config --global user.email "your-email@example.com"
```

## ধাপ ২: GitHub এ Repository তৈরি করুন

1. এই লিঙ্কে যান: https://github.com/new
2. **Repository name**: `bulk-product-upload`
3. **Description**: `REST API for bulk product image upload with Spring Boot and Next.js frontend`
4. **Public** সিলেক্ট করুন
5. **DO NOT** initialize with README, .gitignore, or license (কারণ আমাদের project-এ ইতিমধ্যে আছে)
6. **Create repository** ক্লিক করুন

## ধাপ ৩: Project Push করুন

### Option 1: Batch Script ব্যবহার করুন (সহজ)

```bash
push-to-github.bat
```

### Option 2: Manual Commands

```bash
# Git initialize (যদি না হয়ে থাকে)
git init

# সব files add করুন
git add .

# Commit করুন
git commit -m "Initial commit: Bulk Product Upload API with Spring Boot and Next.js frontend"

# Remote add করুন
git remote add origin https://github.com/CodingManiac44/bulk-product-upload.git

# Branch name set করুন
git branch -M main

# Push করুন
git push -u origin main
```

## Authentication

যদি push করার সময় authentication চায়:

1. **Personal Access Token** ব্যবহার করুন (recommended):
   - https://github.com/settings/tokens এ যান
   - "Generate new token (classic)" ক্লিক করুন
   - `repo` scope select করুন
   - Token generate করুন
   - Push করার সময় password-এর জায়গায় এই token ব্যবহার করুন

2. অথবা **GitHub CLI** ব্যবহার করুন:
   ```bash
   winget install GitHub.cli
   gh auth login
   ```

## Repository URL

Push করার পর আপনার repository এই URL-এ থাকবে:
**https://github.com/CodingManiac44/bulk-product-upload**

## Troubleshooting

- **"Repository not found"**: GitHub এ repository তৈরি করেছেন কিনা check করুন
- **"Authentication failed"**: Personal Access Token ব্যবহার করুন
- **"Git not found"**: Git install করুন: `winget install Git.Git`

