(function () {
  const loader = document.createElement('div');
  loader.className = 'page-loader';
  document.body.prepend(loader);

  function showLoader() {
    loader.classList.add('active');
  }

  document.addEventListener('submit', function (e) {
    if (e.defaultPrevented) return;
    showLoader();
    const btn = e.target.querySelector('button[type="submit"]');
    if (btn) {
      btn.disabled = true;
      btn.classList.add('is-loading');
    }
  });

  document.querySelectorAll('a[href]').forEach(function (link) {
    if (link.target === '_blank' || link.getAttribute('href').startsWith('#')) return;
    link.addEventListener('click', function () {
      showLoader();
      link.classList.add('is-loading');
    });
  });

  const filterBtns = document.querySelectorAll('.filter-btn');
  if (filterBtns.length) {
    const taskItems = document.querySelectorAll('.task-item[data-completed]');
    filterBtns.forEach(function (btn) {
      btn.addEventListener('click', function () {
        filterBtns.forEach(function (b) { b.classList.remove('active'); });
        btn.classList.add('active');
        const filter = btn.dataset.filter;

        taskItems.forEach(function (item) {
          const completed = item.dataset.completed === 'true';
          const show = filter === 'all'
            || (filter === 'active' && !completed)
            || (filter === 'completed' && completed);
          item.classList.toggle('hidden-by-filter', !show);
        });
      });
    });
  }
})();
