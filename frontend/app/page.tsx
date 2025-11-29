'use client'

import { useState, useRef, useCallback, useEffect } from 'react'
import axios from 'axios'

interface Product {
  id: string
  name: string
  description: string
  imageUrl: string
  createdAt: string
  fileSize: number
  contentType: string
}

interface ProductResponse {
  products: Product[]
  totalPages: number
  totalElements: number
  currentPage: number
  pageSize: number
}

const API_BASE_URL = 'http://localhost:8080/api/products'

export default function Home() {
  const [files, setFiles] = useState<File[]>([])
  const [uploadProgress, setUploadProgress] = useState(0)
  const [isUploading, setIsUploading] = useState(false)
  const [uploadMessage, setUploadMessage] = useState<{ type: 'success' | 'error', text: string } | null>(null)
  const [products, setProducts] = useState<Product[]>([])
  const [pagination, setPagination] = useState({ currentPage: 0, totalPages: 0, totalElements: 0, pageSize: 10 })
  const [isLoading, setIsLoading] = useState(false)
  const [isDragging, setIsDragging] = useState(false)
  const fileInputRef = useRef<HTMLInputElement>(null)

  const loadProducts = useCallback(async (page: number = 0) => {
    setIsLoading(true)
    try {
      const response = await axios.get<ProductResponse>(`${API_BASE_URL}?page=${page}&size=${pagination.pageSize}`)
      setProducts(response.data.products)
      setPagination({
        currentPage: response.data.currentPage,
        totalPages: response.data.totalPages,
        totalElements: response.data.totalElements,
        pageSize: response.data.pageSize
      })
    } catch (error) {
      console.error('Error loading products:', error)
      setUploadMessage({ type: 'error', text: 'Failed to load products' })
    } finally {
      setIsLoading(false)
    }
  }, [pagination.pageSize])

  const handleFileSelect = (selectedFiles: FileList | null) => {
    if (selectedFiles) {
      const fileArray = Array.from(selectedFiles)
      setFiles(fileArray)
      setUploadMessage(null)
    }
  }

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault()
    setIsDragging(true)
  }

  const handleDragLeave = () => {
    setIsDragging(false)
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    setIsDragging(false)
    handleFileSelect(e.dataTransfer.files)
  }

  const handleUpload = async () => {
    if (files.length === 0) {
      setUploadMessage({ type: 'error', text: 'Please select files to upload' })
      return
    }

    setIsUploading(true)
    setUploadProgress(0)
    setUploadMessage(null)

    const formData = new FormData()
    files.forEach(file => {
      formData.append('files', file)
    })

    try {
      const response = await axios.post(`${API_BASE_URL}/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (progressEvent) => {
          if (progressEvent.total) {
            const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
            setUploadProgress(percentCompleted)
          }
        },
      })

      setUploadMessage({ type: 'success', text: `Successfully uploaded ${response.data.count} product(s)!` })
      setFiles([])
      setUploadProgress(0)
      await loadProducts(0)
    } catch (error: any) {
      setUploadMessage({ 
        type: 'error', 
        text: error.response?.data?.message || 'Failed to upload products' 
      })
      setUploadProgress(0)
    } finally {
      setIsUploading(false)
    }
  }

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < pagination.totalPages) {
      loadProducts(newPage)
    }
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
  }

  // Load products on mount
  useEffect(() => {
    loadProducts(0)
  }, [])

  return (
    <div className="container">
      <div className="header">
        <h1>Bulk Product Upload</h1>
        <p>Upload multiple product images at once</p>
      </div>

      <div className="upload-section">
        <div
          className={`upload-area ${isDragging ? 'dragover' : ''}`}
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
          onDrop={handleDrop}
          onClick={() => fileInputRef.current?.click()}
        >
          <input
            ref={fileInputRef}
            type="file"
            multiple
            accept="image/*"
            className="file-input"
            onChange={(e) => handleFileSelect(e.target.files)}
          />
          <p style={{ fontSize: '1.2rem', marginBottom: '10px' }}>📁 Drop images here or click to select</p>
          <p style={{ color: '#666' }}>Select multiple images to upload</p>
          {files.length > 0 && (
            <p style={{ marginTop: '15px', color: '#667eea', fontWeight: 'bold' }}>
              {files.length} file(s) selected
            </p>
          )}
        </div>

        {files.length > 0 && (
          <div style={{ marginTop: '20px' }}>
            <ul style={{ listStyle: 'none', padding: 0 }}>
              {files.map((file, index) => (
                <li key={index} style={{ padding: '5px 0', color: '#666' }}>
                  {file.name} ({formatFileSize(file.size)})
                </li>
              ))}
            </ul>
          </div>
        )}

        <button
          className="upload-button"
          onClick={handleUpload}
          disabled={isUploading || files.length === 0}
        >
          {isUploading ? 'Uploading...' : 'Upload Products'}
        </button>

        {isUploading && (
          <div className="progress-container">
            <div className="progress-bar">
              <div className="progress-fill" style={{ width: `${uploadProgress}%` }}>
                {uploadProgress}%
              </div>
            </div>
          </div>
        )}

        {uploadMessage && (
          <div className={uploadMessage.type === 'success' ? 'success' : 'error'}>
            {uploadMessage.text}
          </div>
        )}
      </div>

      <div className="products-section">
        <div className="products-header">
          <h2>Products ({pagination.totalElements})</h2>
          <button
            className="upload-button"
            onClick={() => loadProducts(pagination.currentPage)}
            style={{ padding: '8px 20px', fontSize: '0.9rem' }}
          >
            Refresh
          </button>
        </div>

        {isLoading ? (
          <div className="loading">Loading products...</div>
        ) : products.length === 0 ? (
          <div className="loading">No products found. Upload some images to get started!</div>
        ) : (
          <>
            <div className="products-grid">
              {products.map((product) => (
                <div
                  key={product.id}
                  className="product-card"
                  onClick={() => window.open(product.imageUrl, '_blank')}
                >
                  <img
                    src={product.imageUrl}
                    alt={product.name}
                    className="product-image"
                    onError={(e) => {
                      (e.target as HTMLImageElement).src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="200" height="200"%3E%3Crect fill="%23ddd" width="200" height="200"/%3E%3Ctext fill="%23999" font-family="sans-serif" font-size="14" dy="10.5" font-weight="bold" x="50%25" y="50%25" text-anchor="middle"%3ENo Image%3C/text%3E%3C/svg%3E'
                    }}
                  />
                  <div className="product-info">
                    <div className="product-name">{product.name}</div>
                    <div className="product-description">{product.description}</div>
                    <div className="product-date">{formatDate(product.createdAt)}</div>
                    <div className="product-date" style={{ marginTop: '5px' }}>
                      {formatFileSize(product.fileSize || 0)}
                    </div>
                  </div>
                </div>
              ))}
            </div>

            {pagination.totalPages > 1 && (
              <div className="pagination">
                <button
                  className="pagination-button"
                  onClick={() => handlePageChange(pagination.currentPage - 1)}
                  disabled={pagination.currentPage === 0}
                >
                  Previous
                </button>
                <span className="pagination-info">
                  Page {pagination.currentPage + 1} of {pagination.totalPages}
                </span>
                <button
                  className="pagination-button"
                  onClick={() => handlePageChange(pagination.currentPage + 1)}
                  disabled={pagination.currentPage >= pagination.totalPages - 1}
                >
                  Next
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  )
}

