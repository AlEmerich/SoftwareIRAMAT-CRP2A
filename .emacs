(custom-set-variables
 ;; custom-set-variables was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 '(ansi-color-faces-vector
   [default default default italic underline success warning error])
 '(cua-mode t nil (cua-base))
 '(custom-enabled-themes (quote (tango-dark)))
 '(inhibit-startup-screen t))
(custom-set-faces
 ;; custom-set-faces was added by Custom.
 ;; If you edit it by hand, you could mess it up, so be careful.
 ;; Your init file should contain only one such instance.
 ;; If there is more than one, they won't work right.
 )

(add-hook 'prog-mode-hook 'linum-mode)

(add-to-list 'load-path "~/.emacs.d/elpa/company-0.8.12/")
(autoload 'company-mode "company" nil t)
(add-hook 'after-init-hook 'global-company-mode)

(add-to-list 'load-path "~/.emacs.d/elpa/yasnippet-0.10.0/")
(require 'yasnippet)
(yas/global-mode 1)

(add-to-list 'auto-mode-alist '("\\.cu\\'" . c++-mode))

(yas-global-mode 1)
(add-to-list 'load-path "~/.emacs.d/elpa/php-mode/")
(add-to-list 'load-path "~/.emacs.d/elpa/php-mode/skeleton/")

(autoload 'php-mode "php-mode" "Major mode for editing php code." t)
(add-to-list 'auto-mode-alist '("\\.php$" . php-mode))
(add-to-list 'auto-mode-alist '("\\.inc$" . php-mode))

(require 'php-mode)

(eval-after-load 'php-mode
  '(require 'php-ext))
