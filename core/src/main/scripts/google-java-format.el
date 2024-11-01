;;; woowacourse-java-format.el --- Format code with woowacourse-java-format -*- lexical-binding: t; -*-
;;
;; Copyright 2024 The Woowacourse Java Format Authors. All Rights Reserved.
;; Copyright 2015 Google, Inc. All Rights Reserved.
;;
;; Licensed under the Apache License, Version 2.0 (the "License");
;; you may not use this file except in compliance with the License.
;; You may obtain a copy of the License at
;;
;;      http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required `by applicable law or agreed to in writing, software
;; distributed under the License is distributed on an "AS-IS" BASIS,
;; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
;; See the License for the specific language governing permissions and
;; limitations under the License.

;; Keywords: tools, Java
;; Version: 0.1.0
;; Package-Requires: ((emacs "24"))

;;; Commentary:

;; This package allows a user to filter code through
;; woowacourse-java-format, fixing its formatting.

;; To use it, ensure the directory of this file is in your `load-path'
;; and add
;;
;;   (require 'woowacourse-java-format)
;;
;; to your .emacs configuration.

;; You may also want to bind `woowacourse-java-format-region' to a key:
;;
;;   (global-set-key [C-M-tab] #'woowacourse-java-format-region)

;;; Code:

(defgroup woowacourse-java-format nil
  "Format code using woowacourse-java-format."
  :group 'tools)

(defcustom woowacourse-java-format-executable
  "/usr/bin/woowacourse-java-format"
  "Location of the woowacourse-java-format executable.

A string containing the name or the full path of the executable."
  :group 'woowacourse-java-format
  :type '(file :must-match t :match (lambda (widget file) (file-executable-p file)))
  :risky t)

(defcustom woowacourse-java-format-arguments
  '()
  "Arguments to pass into woowacourse-java-format-executable"
  :group 'woowacourse-java-format
  :type '(repeat string)
  :risky t)

;;;###autoload
(defun woowacourse-java-format-region (start end)
  "Use woowacourse-java-format to format the code between START and END.
If called interactively, uses the region, if there is one.  If
there is no region, then formats the current line."
  (interactive
   (if (use-region-p)
       (list (region-beginning) (region-end))
     (list (point) (1+ (point)))))
  (let ((cursor (point))
        (temp-buffer (generate-new-buffer " *woowacourse-java-format-temp*"))
        (stderr-file (make-temp-file "woowacourse-java-format")))
    (unwind-protect
        (let ((status (apply #'call-process-region
                             ;; Note that emacs character positions are 1-indexed,
                             ;; and woowacourse-java-format is 0-indexed, so we have to
                             ;; subtract 1 from START to line it up correctly.
                             (point-min) (point-max)
                             woowacourse-java-format-executable
                             nil (list temp-buffer stderr-file) t
                             (append woowacourse-java-format-arguments
                                     `("--offset" ,(number-to-string (1- start))
                                       "--length" ,(number-to-string (- end start))
                                       "-"))))
              (stderr
               (with-temp-buffer
                 (insert-file-contents stderr-file)
                 (when (> (point-max) (point-min))
                   (insert ": "))
                 (buffer-substring-no-properties
                  (point-min) (line-end-position)))))
          (cond
           ((stringp status)
            (error "woowacourse-java-format killed by signal %s%s" status stderr))
           ((not (zerop status))
            (error "woowacourse-java-format failed with code %d%s" status stderr))
           (t (message "woowacourse-java-format succeeded%s" stderr)
              (delete-region (point-min) (point-max))
              (insert-buffer-substring temp-buffer)
              (goto-char cursor))))
      (delete-file stderr-file)
      (when (buffer-name temp-buffer) (kill-buffer temp-buffer)))))

;;;###autoload
(defun woowacourse-java-format-buffer ()
  "Use woowacourse-java-format to format the current buffer."
  (interactive)
  (woowacourse-java-format-region (point-min) (point-max)))

;;;###autoload
(defalias 'woowacourse-java-format 'woowacourse-java-format-region)

(provide 'woowacourse-java-format)
;;; woowacourse-java-format.el ends here
