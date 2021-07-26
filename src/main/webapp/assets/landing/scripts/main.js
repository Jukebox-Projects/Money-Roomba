window.onload = function () {
  var elements = document.getElementsByClassName('typewrite');
  for (var i = 0; i < elements.length; i++) {
    var toRotate = elements[i].getAttribute('data-type');
    var period = elements[i].getAttribute('data-period');
    if (toRotate) {
      new TxtType(elements[i], JSON.parse(toRotate), period);
    }
  }
  // INJECT CSS
  var css = document.createElement('style');
  css.type = 'text/css';
  css.innerHTML = '.typewrite > .wrap { border-right: 0.08em solid #fff}';
  document.body.appendChild(css);
};

var scrollWindow = function () {
  $(window).scroll(function () {
    var $w = $(this),
      st = $w.scrollTop(),
      navbar = $('.ftco_navbar'),
      sd = $('.js-scroll-wrap');

    if (st > 150) {
      if (!navbar.hasClass('scrolled')) {
        navbar.addClass('scrolled');
      }
    }
    if (st < 150) {
      if (navbar.hasClass('scrolled')) {
        navbar.removeClass('scrolled sleep');
      }
    }
    if (st > 350) {
      if (!navbar.hasClass('awake')) {
        navbar.addClass('awake');
      }

      if (sd.length > 0) {
        sd.addClass('sleep');
      }
    }
    if (st < 350) {
      if (navbar.hasClass('awake')) {
        navbar.removeClass('awake');
        navbar.addClass('sleep');
      }
      if (sd.length > 0) {
        sd.removeClass('sleep');
      }
    }
  });
};
scrollWindow();
