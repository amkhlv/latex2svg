#!/usr/bin/env racket

#lang racket

(require net/http-client)
(require net/url)
(require net/url-structs)
(require racket/cmdline)

(define host (make-parameter "127.0.0.1"))
(define port (make-parameter 9000))
(define latex (make-parameter ""))
(define outfile (make-parameter #f))
(define size (make-parameter "20"))
(define bg (make-parameter "200:200:200"))
(define fg (make-parameter "100:0:0"))

(latex
 (command-line
  #:once-each
  ["--host" host-name "Host to connect to" (host host-name)]
  [("-p" "--port") port-number "Port to listen on" (port (string->number port-number))]
  [("-o" "--out-file") out-file "File to write SVG to (default do not write, just print on STDOUT)" (outfile out-file)]
  [("-s" "--size") formula-size "Size" (size formula-size)]
  ["--bg" formula-bg "bg color, example: 200:200:200" (bg formula-bg)]
  ["--fg" formula-fg "fg color, example: 100:0:0" (fg formula-fg)]
  #:usage-help "\nrequest convertion of LaTeX to SVG\n"
  #:args latex-string
  latex-string))

(define svg-server-conn (http-conn-open "127.0.0.1" #:port (port)))

(define (get-svg lat)
  (let* ([u (url
             "http" ; scheme
             #f     ; user
             (host)   ;host 
             (port)   ;port
             #t          ;path-absolute?
             (list (path/param "svg" '()))        ;path
             (list (cons 'latex lat) (cons 'size (size)) (cons 'bg (bg)) (cons 'fg (fg))) ;query
             #f ;fragment
             )])
    (display (url->string u))
    (display "\n")
    (http-conn-sendrecv! svg-server-conn (url->string u) #:method #"POST")
    ))

(let-values ([(status headers inport) (get-svg (car (latex)))])
  (for/list ([h headers]) (display h) (display "\n -------------- \n"))
  (let ((result (port->string inport)))
    (if (outfile)
        (with-output-to-file #:exists 'replace (outfile)
          (lambda () (display result)))
        (display result)))
  (close-input-port inport))

(http-conn-close! svg-server-conn)
