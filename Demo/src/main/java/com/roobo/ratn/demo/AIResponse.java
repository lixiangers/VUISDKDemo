package com.roobo.ratn.demo;

import com.roobo.ratn.demo.util.JsonUtil;

import java.util.Arrays;

/**
 * Created by lixiang on 18-1-11.
 */

public class AIResponse {

    /**
     * apiVersion : 1.0
     * status : {"code":0,"errorType":"success"}
     * asr : {"text":"What's the time"}
     * tts : {"text":"我的名字叫，好听吗？","format":"mp3","content":"http://ros.roobo.net/tvoice/static/usertts/2018-01-11/3/reply.16373907323520864092.37934a72-a470-4f6c-9708-c7b655f78d04.mp3","music":"","cookie":""}
     * ai : {"status":{"code":0,"errorType":"success"},"query":"what's the time","semantic":{"service":"Chat","action":"default","outputContext":{"context":"chat","service":"Chat"}},"result":{"hint":"我的名字叫，好听吗？","data":{"emotion":[{"score":"","type":"text_question","value":"normal"},{"score":"","type":"text_answer","value":"normal"}],"source":"chat_roobo_children"}}}
     */

    private String apiVersion;
    private StatusBean status;
    private AsrBean asr;
    private TtsBean tts;
    private AiBean ai;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public AsrBean getAsr() {
        return asr;
    }

    public void setAsr(AsrBean asr) {
        this.asr = asr;
    }

    public TtsBean getTts() {
        return tts;
    }

    public void setTts(TtsBean tts) {
        this.tts = tts;
    }

    public AiBean getAi() {
        return ai;
    }

    public void setAi(AiBean ai) {
        this.ai = ai;
    }

    public String getoutputContext() {
        if (getAi() == null)
            return null;
        AiBean.SemanticBean.OutputContextBean outputContext = getAi().getSemantic().getOutputContext();
        //格式必须是[{/"context":"", "service":"" }]
        return JsonUtil.toJsonString(Arrays.asList(outputContext));
    }

    public static class StatusBean {
        /**
         * code : 0
         * errorType : success
         */

        private int code;
        private String errorType;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }
    }

    public static class AsrBean {
        /**
         * text : What's the time
         */

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class TtsBean {
        /**
         * text : 我的名字叫，好听吗？
         * format : mp3
         * content : http://ros.roobo.net/tvoice/static/usertts/2018-01-11/3/reply.16373907323520864092.37934a72-a470-4f6c-9708-c7b655f78d04.mp3
         * music :
         * cookie :
         */

        private String text;
        private String format;
        private String content;
        private String music;
        private String cookie;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMusic() {
            return music;
        }

        public void setMusic(String music) {
            this.music = music;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }
    }

    public static class AiBean {
        /**
         * status : {"code":0,"errorType":"success"}
         * query : what's the time
         * semantic : {"service":"Chat","action":"default","outputContext":{"context":"chat","service":"Chat"}}
         * result : {"hint":"我的名字叫，好听吗？","data":{"emotion":[{"score":"","type":"text_question","value":"normal"},{"score":"","type":"text_answer","value":"normal"}],"source":"chat_roobo_children"}}
         */

        private StatusBeanX status;
        private String query;
        private SemanticBean semantic;

        public StatusBeanX getStatus() {
            return status;
        }

        public void setStatus(StatusBeanX status) {
            this.status = status;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public SemanticBean getSemantic() {
            return semantic;
        }

        public void setSemantic(SemanticBean semantic) {
            this.semantic = semantic;
        }

        public static class StatusBeanX {
            /**
             * code : 0
             * errorType : success
             */

            private int code;
            private String errorType;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getErrorType() {
                return errorType;
            }

            public void setErrorType(String errorType) {
                this.errorType = errorType;
            }
        }

        public static class SemanticBean {
            /**
             * service : Chat
             * action : default
             * outputContext : {"context":"chat","service":"Chat"}
             */

            private String service;
            private String action;
            private OutputContextBean outputContext;

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public OutputContextBean getOutputContext() {
                return outputContext;
            }

            public void setOutputContext(OutputContextBean outputContext) {
                this.outputContext = outputContext;
            }

            public static class OutputContextBean {
                /**
                 * context : chat
                 * service : Chat
                 */

                private String context;
                private String service;

                public String getContext() {
                    return context;
                }

                public void setContext(String context) {
                    this.context = context;
                }

                public String getService() {
                    return service;
                }

                public void setService(String service) {
                    this.service = service;
                }
            }
        }

    }
}
