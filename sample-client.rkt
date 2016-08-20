#!/usr/bin/env racket

#lang racket

(require net/http-client)
(require net/url)
(require net/url-structs)
(require racket/cmdline)
(require xml xml/path)

(define conf (make-parameter (path->string (build-path (find-system-path 'home-dir) ".config" "amkhlv" "latex2svg.xml"))))
(define latex (make-parameter #f))
(define outfile (make-parameter #f))
(define bibkey (make-parameter #f))
(define size (make-parameter "20"))
(define bg (make-parameter "200:200:200"))
(define fg (make-parameter "100:0:0"))

(latex
 (command-line
  #:once-each
  [("-c" "--conf") conf-file "Configuration file" (conf conf-file)]
  [("-b" "--bibkey") b "Bibkey" (bibkey b)]
  [("-o" "--out-file") out-file "File to write SVG to (default do not write, just print on STDOUT)" (outfile out-file)]
  [("-s" "--size") formula-size "Size" (size formula-size)]
  ["--bg" formula-bg "bg color, example: 200:200:200" (bg formula-bg)]
  ["--fg" formula-fg "fg color, example: 100:0:0" (fg formula-fg)]
  #:usage-help "\nrequest convertion of LaTeX to SVG\n"
  #:args latex-string
  latex-string))

(define conf-xml 
  (call-with-input-file (conf)
    (lambda (inport) (xml->xexpr (document-element (read-xml inport))))))
(define host (se-path* '(host) conf-xml))
(define port (string->number (se-path* '(port) conf-xml)))
(define token (se-path* '(token) conf-xml))

(define svg-server-conn (http-conn-open host #:port port))

(define (get-svg lat)
  (let* ([u (url
             "http" ; scheme
             #f     ; user
             host   ;host 
             port   ;port
             #t          ;path-absolute?
             (list (path/param (se-path* '(path) conf-xml) '()))        ;path
             (list (cons 'token token) (cons 'latex lat) (cons 'size (size)) (cons 'bg (bg)) (cons 'fg (fg))) ;query
             #f ;fragment
             )])
    (display (url->string u))
    (display "\n")
    (http-conn-sendrecv! svg-server-conn (url->string u) #:method #"POST" #:headers '("BystroTeX:yes"))
    ))

(define (get-bibtex b)
  (let* ([u (url
             "http" ; scheme
             #f     ; user
             host   ;host 
             port   ;port
             #t          ;path-absolute?
             (list (path/param (se-path* '(bibpath) conf-xml) '()))        ;path
             (list (cons 'token token) (cons 'k b)) ;query
             #f ;fragment
             )])
    (display (url->string u))
    (display "\n")
    (http-conn-sendrecv! svg-server-conn (url->string u) #:method #"POST" #:headers '("BystroTeX:yes"))
    ))


(when (cons? (latex))
    (let-values ([(status headers inport) (get-svg (car (latex)))])
      (for/list ([h headers]) (display h) (display "\n -------------- \n"))
      (let ((result (port->string inport)))
        (if (outfile)
            (with-output-to-file #:exists 'replace (outfile)
                                 (lambda () (display result)))
            (display result)))
      (close-input-port inport)))

(when (bibkey)
    (let-values ([(status headers inport) (get-bibtex (bibkey))])
      (for/list ([h headers]) (display h) (display "\n -------------- \n"))
      (let ((result (port->string inport)))
        (display result))
      (close-input-port inport)))

(http-conn-close! svg-server-conn)
