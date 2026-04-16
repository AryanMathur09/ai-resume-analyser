import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Upload, FileText, CheckCircle, Loader2, Lock, Mail, User, LogOut } from 'lucide-react';

function App() {
  // --- STATE ---
  const [token, setToken] = useState(localStorage.getItem('jwt_token') || null);
  const [authMode, setAuthMode] = useState('login'); // 'login' or 'register'

  // Auth Form State
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [authError, setAuthError] = useState('');

  // Dashboard State
  const [file, setFile] = useState(null);
  const [jd, setJd] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  // --- HANDLERS ---
  const handleAuth = async (e) => {
    e.preventDefault();
    setLoading(true);
    setAuthError('');

    try {
      if (authMode === 'login') {
        const response = await axios.post('http://localhost:8080/api/auth/login', { email, password });
        // Assuming your backend returns { "jwt": "token_string" } or just the token string
        const receivedToken = response.data.jwt || response.data;
        setToken(receivedToken);
        localStorage.setItem('jwt_token', receivedToken);
      } else {
        await axios.post('http://localhost:8080/api/auth/register', { username, email, password });
        setAuthMode('login');
        alert("Registration successful! Please log in.");
      }
    } catch (error) {
      setAuthError(error.response?.data?.message || "Authentication failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    setToken(null);
    localStorage.removeItem('jwt_token');
    setResult(null);
    setFile(null);
    setJd('');
  };

  const handleUpload = async () => {
    if (!file || !jd) return alert("Please provide both a resume and a JD!");

    setLoading(true);
    const formData = new FormData();
    formData.append('file', file);
    formData.append('jobDescription', jd);

    try {
      const response = await axios.post('http://localhost:8080/api/resume/upload', formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });
      setResult(response.data);
    } catch (error) {
      console.error("Upload failed", error);
      if (error.response?.status === 403) {
        alert("Session expired. Please log in again.");
        handleLogout();
      } else {
        alert("Analysis failed. Check if Backend & AI Engine are running!");
      }
    } finally {
      setLoading(false);
    }
  };

  // --- UI: AUTHENTICATION SCREEN ---
  if (!token) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 flex items-center justify-center p-4 font-sans">
        <div className="bg-white p-8 rounded-3xl shadow-xl shadow-slate-200/50 border border-white w-full max-w-md animate-in zoom-in-95 duration-500">
          <div className="text-center mb-8">
            <div className="inline-block p-3 bg-blue-600 rounded-2xl mb-4 shadow-lg shadow-blue-200">
              <Lock className="text-white" size={28} />
            </div>
            <h1 className="text-3xl font-extrabold text-slate-900">Welcome Back</h1>
            <p className="text-slate-500 mt-2">Sign in to your AI workspace</p>
          </div>

          <form onSubmit={handleAuth} className="space-y-5">
            {authError && <div className="p-3 bg-red-50 text-red-600 rounded-xl text-sm font-medium text-center">{authError}</div>}

            {authMode === 'register' && (
              <div className="relative">
                <User className="absolute left-3 top-3.5 text-slate-400" size={20} />
                <input
                  type="text" required placeholder="Full Name"
                  value={username} onChange={(e) => setUsername(e.target.value)}
                  className="w-full pl-10 p-3 border border-slate-200 rounded-xl focus:ring-4 focus:ring-blue-100 focus:border-blue-400 outline-none transition-all bg-slate-50"
                />
              </div>
            )}

            <div className="relative">
              <Mail className="absolute left-3 top-3.5 text-slate-400" size={20} />
              <input
                type="email" required placeholder="Email Address"
                value={email} onChange={(e) => setEmail(e.target.value)}
                className="w-full pl-10 p-3 border border-slate-200 rounded-xl focus:ring-4 focus:ring-blue-100 focus:border-blue-400 outline-none transition-all bg-slate-50"
              />
            </div>

            <div className="relative">
              <Lock className="absolute left-3 top-3.5 text-slate-400" size={20} />
              <input
                type="password" required placeholder="Password"
                value={password} onChange={(e) => setPassword(e.target.value)}
                className="w-full pl-10 p-3 border border-slate-200 rounded-xl focus:ring-4 focus:ring-blue-100 focus:border-blue-400 outline-none transition-all bg-slate-50"
              />
            </div>

            <button disabled={loading} type="submit" className="w-full bg-blue-600 text-white py-3.5 rounded-xl font-bold hover:bg-blue-700 transition-all flex items-center justify-center gap-2">
              {loading ? <Loader2 className="animate-spin" /> : (authMode === 'login' ? 'Sign In' : 'Create Account')}
            </button>
          </form>

          <p className="text-center mt-6 text-sm text-slate-500 font-medium">
            {authMode === 'login' ? "Don't have an account? " : "Already have an account? "}
            <button onClick={() => setAuthMode(authMode === 'login' ? 'register' : 'login')} className="text-blue-600 hover:underline">
              {authMode === 'login' ? 'Register here' : 'Login here'}
            </button>
          </p>
        </div>
      </div>
    );
  }

  // --- UI: DASHBOARD SCREEN ---
  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 p-4 md:p-8 font-sans">
      <div className="max-w-6xl mx-auto">

        {/* Header Section */}
        <header className="mb-10 text-center animate-in fade-in slide-in-from-top-4 duration-700 relative">
          <button onClick={handleLogout} className="absolute right-0 top-0 flex items-center gap-2 text-slate-500 hover:text-red-500 transition-colors font-semibold bg-white px-4 py-2 rounded-full shadow-sm border border-slate-100">
            <LogOut size={16} /> Logout
          </button>

          <div className="inline-block p-3 bg-blue-600 rounded-2xl mb-4 shadow-lg shadow-blue-200">
            <FileText className="text-white" size={32} />
          </div>
          <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight">
            AI Resume <span className="text-blue-600">Analyzer</span>
          </h1>
          <p className="text-slate-500 mt-2 text-lg">Instant ATS optimization powered by Gemini 2.5</p>
        </header>

        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">

          {/* Left Column: Inputs */}
          <div className="lg:col-span-5 space-y-6">
            <div className="bg-white/80 backdrop-blur-md p-8 rounded-3xl shadow-xl shadow-slate-200/50 border border-white">
              <h2 className="text-xl font-bold text-slate-800 mb-6 flex items-center gap-2">
                <Upload size={22} className="text-blue-500" /> Upload Workspace
              </h2>

              <div className="space-y-5">
                <div>
                  <label className="block text-sm font-semibold text-slate-600 mb-2">Resume Document</label>
                  <input type="file" onChange={(e) => setFile(e.target.files[0])} className="block w-full text-sm text-slate-500 file:mr-4 file:py-3 file:px-6 file:rounded-xl file:border-0 file:text-sm file:font-bold file:bg-blue-600 file:text-white hover:file:bg-blue-700 transition-all cursor-pointer bg-slate-50 rounded-xl border border-slate-100" />
                </div>

                <div>
                  <label className="block text-sm font-semibold text-slate-600 mb-2">Target Job Description</label>
                  <textarea rows="8" value={jd} onChange={(e) => setJd(e.target.value)} className="w-full p-4 border border-slate-200 rounded-2xl focus:ring-4 focus:ring-blue-100 focus:border-blue-400 outline-none transition-all bg-slate-50/50 text-slate-700 placeholder:text-slate-400" placeholder="Paste the requirements you're aiming for..." />
                </div>

                <button onClick={handleUpload} disabled={loading} className="w-full bg-slate-900 text-white py-4 rounded-2xl font-bold hover:bg-black hover:shadow-xl hover:-translate-y-0.5 transition-all flex items-center justify-center gap-3 disabled:bg-slate-300 disabled:transform-none">
                  {loading ? <Loader2 className="animate-spin" /> : 'Run AI Analysis'}
                </button>
              </div>
            </div>
          </div>

          {/* Right Column: Results */}
          <div className="lg:col-span-7">
            <div className="bg-white p-8 rounded-3xl shadow-xl shadow-slate-200/50 border border-slate-100 min-h-[500px]">
              <h2 className="text-xl font-bold text-slate-800 mb-6 flex items-center gap-2">
                <CheckCircle size={22} className="text-green-500" /> Analysis Intelligence
              </h2>

              {!result && !loading && (
                <div className="h-full flex flex-col items-center justify-center py-20 text-slate-300 border-2 border-dashed border-slate-100 rounded-2xl">
                  <div className="bg-slate-50 p-6 rounded-full mb-4">
                    <FileText size={60} className="opacity-20" />
                  </div>
                  <p className="font-medium">Waiting for your resume data...</p>
                </div>
              )}

              {loading && (
                <div className="flex flex-col items-center justify-center py-20 space-y-4">
                  <Loader2 size={60} className="animate-spin text-blue-600" />
                  <p className="text-blue-600 font-bold animate-pulse">Gemini is thinking...</p>
                </div>
              )}

              {result && (
                <div className="space-y-8 animate-in zoom-in-95 duration-500">
                  <div className="bg-gradient-to-r from-blue-600 to-indigo-700 p-8 rounded-2xl text-white shadow-lg shadow-blue-200">
                    <div className="flex items-end justify-between">
                      <div>
                        <p className="text-blue-100 text-sm font-bold uppercase tracking-widest">Match Accuracy</p>
                        <h3 className="text-5xl font-black mt-1">{result.resumeScore}%</h3>
                      </div>
                      <div className="text-right">
                        <p className="text-xs text-blue-100 mb-1 font-medium">Status</p>
                        <span className="bg-white/20 px-3 py-1 rounded-full text-xs font-bold backdrop-blur-sm">
                          {result.resumeScore > 80 ? 'EXCELLENT' : 'NEEDS WORK'}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div>
                    <h3 className="text-xs font-black text-slate-400 uppercase tracking-[0.2em] mb-4">Core Skills Detected</h3>
                    <div className="flex flex-wrap gap-2">
                      {result.skillsFound.map(skill => (
                        <span key={skill} className="bg-slate-900 text-white px-4 py-2 rounded-xl text-sm font-bold shadow-sm">
                          {skill}
                        </span>
                      ))}
                    </div>
                  </div>

                  <div className="bg-blue-50/50 border-l-8 border-blue-600 p-6 rounded-r-2xl">
                    <h3 className="text-xs font-black text-blue-600 uppercase tracking-[0.2em] mb-3">AI Executive Summary</h3>
                    <p className="text-slate-700 text-lg leading-relaxed font-medium">
                      "{result.summary}"
                    </p>
                  </div>
                </div>
              )}
            </div>
          </div>

        </div>
      </div>
    </div>
  );
}

export default App;