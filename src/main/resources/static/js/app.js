(function () {
  const appLoader = document.getElementById('appLoader');
  window.addEventListener('load', function () {
    if (appLoader) appLoader.classList.add('hidden');
  });

  const loader = document.createElement('div');
  loader.className = 'page-loader';
  document.body.prepend(loader);

  function showLoader() {
    loader.classList.add('active');
  }

  const deleteModal = document.getElementById('deleteModal');
  const deleteCancelBtn = document.getElementById('deleteCancelBtn');
  const deleteConfirmBtn = document.getElementById('deleteConfirmBtn');
  let formPendingDelete = null;

  function openDeleteModal(form) {
    formPendingDelete = form;
    deleteModal.classList.add('active');
  }

  function closeDeleteModal() {
    formPendingDelete = null;
    deleteModal.classList.remove('active');
  }

  if (deleteModal) {
    deleteCancelBtn.addEventListener('click', closeDeleteModal);
    deleteModal.addEventListener('click', function (e) {
      if (e.target === deleteModal) closeDeleteModal();
    });
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && deleteModal.classList.contains('active')) closeDeleteModal();
    });
    deleteConfirmBtn.addEventListener('click', function () {
      const form = formPendingDelete;
      closeDeleteModal();
      if (form) {
        form.dataset.confirmed = 'true';
        form.requestSubmit();
      }
    });
  }

  document.addEventListener('submit', function (e) {
    if (e.target.classList.contains('delete-form') && e.target.dataset.confirmed !== 'true') {
      e.preventDefault();
      openDeleteModal(e.target);
      return;
    }

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
